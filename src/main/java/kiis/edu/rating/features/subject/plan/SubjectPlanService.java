package kiis.edu.rating.features.subject.plan;

import kiis.edu.rating.enums.Department;
import kiis.edu.rating.features.subject.base.SubjectEntity;
import kiis.edu.rating.features.subject.base.SubjectRepository;
import kiis.edu.rating.features.subject.condition.SubjectConditionEntity;
import kiis.edu.rating.features.subject.condition.SubjectConditionRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static kiis.edu.rating.enums.Department.ALL;
import static kiis.edu.rating.enums.SubjectClassification.Big;
import static kiis.edu.rating.enums.SubjectClassification.Middle;
import static kiis.edu.rating.enums.SubjectClassification.Small;

@Service
@RequiredArgsConstructor
public class SubjectPlanService {
    private final SubjectRepository subjectRepository;
    private final SubjectConditionRepository subjectConditionRepository;

    private Big currentBig;
    private Middle currentMiddle;
    private List<SubjectEntity> subjectList;
    private List<SubjectConditionEntity> conditionList;

    public List<BigGroup> createList() {
        this.subjectList = subjectRepository.findAllByIsDeletedFalse();
        this.conditionList = subjectConditionRepository.findAllByIsDeleted(false);

        return bigList();
    }


    private @NotNull List<BigGroup> bigList() {
        final List<BigGroup> bigGroupList = new ArrayList<>();
        for (final Big big : Big.values()) {
            currentBig = big;

            final List<SubjectEntity> filteredByBig = filter(subjectList,
                    subject -> subject.classification.getMiddle().getBig().equals(big));
            final List<MiddleGroup> middleGroupList = middleList(filteredByBig);
            final BigGroup bigGroup = new BigGroup(big, middleGroupList);
            bigGroupList.add(bigGroup);
        }
        return bigGroupList;
    }

    private List<MiddleGroup> middleList(List<SubjectEntity> filteredByBig) {
        final List<MiddleGroup> middleGroupList = new ArrayList<>();
        for (final Middle middle : Middle.values()) {
            currentMiddle = middle;

            if (!middle.getBig().equals(currentBig)) continue;
            final List<SubjectEntity> filteredByMiddle = filter(filteredByBig,
                    subject -> subject.classification.getMiddle().equals(middle));
            final List<SmallGroup> smallGroupList = smallList(filteredByMiddle);
            final MiddleGroup middleGroup = new MiddleGroup(middle, smallGroupList);
            middleGroupList.add(middleGroup);
        }
        return middleGroupList;
    }

    private List<SmallGroup> smallList(List<SubjectEntity> filteredByMiddle) {
        final List<SmallGroup> smallGroupList = new ArrayList<>();

        for (final Small small : Small.values()) {
            if (!small.getMiddle().equals(currentMiddle)) continue;
            final List<SubjectEntity> filteredBySmall = filter(
                    filteredByMiddle, subject -> subject.classification.equals(small)
                            && (subject.department.equals(small.getDepartment()) || subject.department.equals(ALL))
            );
            final SmallGroup smallGroup = new SmallGroup(small, filteredBySmall, conditionList);
            smallGroupList.add(smallGroup);
        }
        return smallGroupList;
    }

    private static List<SubjectEntity> filter(List<SubjectEntity> subjectEntityList, Predicate<SubjectEntity> predicate) {
        return subjectEntityList.stream().filter(predicate).collect(Collectors.toList());
    }
}

class BigGroup extends Group<Big> {
    public final List<MiddleGroup> middleList;
    public final int creditNeeded;
    public final int requiredCredits;
    public final Map<Department, Integer> rowspan = Department.frame();

    public BigGroup(Big big, List<MiddleGroup> middleList) {
        super(big, big.getLabel());
        this.creditNeeded = big.getCreditNeeded();
        this.middleList = middleList;
        this.middleList.forEach(middle -> middle.rowspan.forEach(this::add));
        this.requiredCredits = middleList.stream()
                .reduce(0, (total, middle) -> total + middle.requiredCredits, Integer::sum);
    }

    private void add(Department department, int row) {
        final Integer pre = this.rowspan.get(department);
        this.rowspan.put(department, pre + row);
    }
}

class MiddleGroup extends Group<Middle> {
    public final List<SmallGroup> smallList;
    public final int creditNeeded;
    public final int requiredCredits;
    public final Map<Department, Integer> rowspan = Department.frame();

    public MiddleGroup(Middle middle, List<SmallGroup> smallList) {
        super(middle, middle.getLabel());
        this.creditNeeded = middle.getCreditNeeded();
        this.smallList = smallList;
        this.fillMap();
        this.requiredCredits = this.countRequiredCredits();
    }

    private void fillMap() {
        this.smallList.forEach(small -> {
            if (!small.department.equals(ALL)) {
                increase(small.department);
            } else {
                Department.getValues().forEach(this::increase);
            }
        });
    }

    private void increase(Department department) {
        final Integer pre = this.rowspan.get(department);
        this.rowspan.put(department, pre + 1);
    }

    private int countRequiredCredits() {
        final AtomicInteger total = new AtomicInteger();
        this.smallList.forEach(small -> small.yearList.forEach(year -> year.forEach(subject -> {
            if (subject.subjectEntity.require) total.addAndGet(subject.subjectEntity.credit);
        })));
        return total.get();
    }
}

class SmallGroup extends Group<Small> {
    public final List<List<SubjectWithCondition>> yearList;
    public final Department department;

    private final List<SubjectConditionEntity> conditionList;
    private final List<SubjectEntity> subjectList;

    public SmallGroup(Small small, List<SubjectEntity> subjectEntityList,
                      List<SubjectConditionEntity> conditionList) {
        super(small, small.getLabel());
        this.department = small.getDepartment();
        this.conditionList = conditionList;
        this.subjectList = subjectEntityList;

        final List<List<SubjectWithCondition>> yearList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            yearList.add(filterByYear(Collections.singletonList(i)));
        }
        yearList.add(filterByYear(Arrays.asList(3, 4)));
        this.yearList = yearList;
    }

    private List<SubjectWithCondition> filterByYear(List<Integer> years) {
        return subjectList.stream()
                .filter(subject -> years.contains(subject.formYear))
                .map(subject -> new SubjectWithCondition(subject, conditionList))
                .collect(Collectors.toList());
    }
}

@AllArgsConstructor
class Group<N> {
    public final N name;
    public final String label;
}


class SubjectWithCondition {
    public final SubjectEntity subjectEntity;
    public final List<Long> conditionList;

    public SubjectWithCondition(SubjectEntity subjectEntity, @NotNull List<SubjectConditionEntity> conditionList) {
        this.subjectEntity = subjectEntity;
        this.conditionList = conditionList.stream()
                .filter(condition -> condition.toId == subjectEntity.id)
                .map(condition -> condition.fromId)
                .collect(Collectors.toList());
    }
}

