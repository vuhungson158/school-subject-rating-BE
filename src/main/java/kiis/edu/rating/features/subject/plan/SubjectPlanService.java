package kiis.edu.rating.features.subject.plan;

import kiis.edu.rating.enums.Department;
import kiis.edu.rating.features.subject.base.SubjectEntity;
import kiis.edu.rating.features.subject.base.SubjectRepository;
import kiis.edu.rating.features.subject.condition.SubjectConditionEntity;
import kiis.edu.rating.features.subject.condition.SubjectConditionRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static kiis.edu.rating.enums.Department.ALL;
import static kiis.edu.rating.enums.SubjectClassification.*;

@Service
@RequiredArgsConstructor
public class SubjectPlanService {
    private final SubjectRepository subjectRepository;
    private final SubjectConditionRepository subjectConditionRepository;

    private Department currentDepartment;
    private Big currentBig;
    private Middle currentMiddle;

    public List<DepartmentGroup> createList() {
        final List<DepartmentGroup> departmentGroupList = new ArrayList<>();
        for (Department department : Department.values()) {
            currentDepartment = department;

            if (department.equals(ALL)) continue;
            final List<SubjectEntity> filteredByDepartment = filter(subjectRepository.findAllByDisable(false),
                    subject -> subject.department.equals(department)
                            || subject.department.equals(ALL));
            DepartmentGroup departmentGroup = new DepartmentGroup(department, bigList(filteredByDepartment));
            departmentGroupList.add(departmentGroup);
        }
        return departmentGroupList;
    }


    private List<BigGroup> bigList(List<SubjectEntity> filteredByDepartment) {
        final List<BigGroup> bigGroupList = new ArrayList<>();
        for (Big big : Big.values()) {
            currentBig = big;

            final List<SubjectEntity> filteredByBig = filter(filteredByDepartment,
                    subject -> subject.classification.getMiddle().getBig().equals(big));
            final List<MiddleGroup> middleGroupList = middleList(filteredByBig);
            int bigRowspan = sumBigRowspan(middleGroupList);
            final BigGroup bigGroup = new BigGroup(big, middleGroupList, bigRowspan);
            bigGroupList.add(bigGroup);
        }
        return bigGroupList;
    }

    private int sumBigRowspan(List<MiddleGroup> middleList) {
        return middleList.stream()
                .reduce(0, (total, middle) -> total + middle.rowspan, Integer::sum);
    }

    private List<MiddleGroup> middleList(List<SubjectEntity> filteredByBig) {
        final List<MiddleGroup> middleGroupList = new ArrayList<>();
        for (Middle middle : Middle.values()) {
            currentMiddle = middle;

            if (!middle.getBig().equals(currentBig)) continue;
            final List<SubjectEntity> filteredByMiddle = filter(filteredByBig,
                    subject -> subject.classification.getMiddle().equals(middle));
            final List<SmallGroup> smallGroupList = smallList(filteredByMiddle);
            int middleRowspan = countMiddleRowspan(smallGroupList);
            final MiddleGroup middleGroup = new MiddleGroup(middle, smallGroupList, middleRowspan);
            middleGroupList.add(middleGroup);
        }
        return middleGroupList;
    }

    private int countMiddleRowspan(List<SmallGroup> smallList) {
        return smallList.size();
    }

    private List<SmallGroup> smallList(List<SubjectEntity> filteredByMiddle) {
        final List<SmallGroup> smallGroupList = new ArrayList<>();

        for (Small small : Small.values()) {
            if (!small.getMiddle().equals(currentMiddle)) continue;
            if (!small.getDepartment().equals(currentDepartment)
                    && !small.getDepartment().equals(ALL)) continue;
            final List<SubjectEntity> filteredBySmall = filter(filteredByMiddle,
                    subject -> subject.classification.equals(small));
            final SmallGroup smallGroup = new SmallGroup(small, filteredBySmall, subjectConditionRepository.findAllByDisable(false));
            smallGroupList.add(smallGroup);
        }
        return smallGroupList;
    }

    private static List<SubjectEntity> filter(List<SubjectEntity> subjectEntityList, Predicate<SubjectEntity> predicate) {
        return subjectEntityList.stream().filter(predicate).collect(Collectors.toList());
    }
}

class DepartmentGroup extends Group<Department> {
    public final List<BigGroup> bigList;

    public DepartmentGroup(Department department, List<BigGroup> bigList) {
        super(department, department.getLabel());
        this.bigList = bigList;
    }
}

class BigGroup extends Group<Big> {
    public final List<MiddleGroup> middleList;
    public final int creditNeeded;
    public final int rowspan;

    public BigGroup(Big big, List<MiddleGroup> middleList, int rowspan) {
        super(big, big.getLabel());
        this.creditNeeded = big.getCreditNeeded();
        this.middleList = middleList;
        this.rowspan = rowspan;
    }
}

class MiddleGroup extends Group<Middle> {
    public final List<SmallGroup> smallList;
    public final int creditNeeded;
    public final int rowspan;

    public MiddleGroup(Middle middle, List<SmallGroup> smallList, int rowspan) {
        super(middle, middle.getLabel());
        this.creditNeeded = middle.getCreditNeeded();
        this.smallList = smallList;
        this.rowspan = rowspan;
    }
}

class SmallGroup extends Group<Small> {
    public final List<List<SubjectWithCondition>> yearList;

    public SmallGroup(Small small, List<SubjectEntity> subjectEntityList,
                      List<SubjectConditionEntity> conditionList) {
        super(small, small.getLabel());

        List<List<SubjectWithCondition>> yearList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            final int year = i;
            List<SubjectWithCondition> subjectListByYear = subjectEntityList.stream()
                    .filter(subject -> subject.formYear == year)
                    .map(subject -> new SubjectWithCondition(subject, conditionList))
                    .collect(Collectors.toList());
            yearList.add(subjectListByYear);

        }
        List<SubjectWithCondition> subjectListByYear = subjectEntityList.stream()
                .filter(subject -> subject.formYear == 3 || subject.formYear == 4)
                .map(subject -> new SubjectWithCondition(subject, conditionList))
                .collect(Collectors.toList());
        yearList.add(subjectListByYear);
        this.yearList = yearList;
    }
}

@AllArgsConstructor
class Group<N> {
    public final N name;
    public final String label;
}

class SubjectWithCondition {
    final public SubjectEntity subjectEntity;
    final public List<Long> condition;

    public SubjectWithCondition(SubjectEntity subjectEntity, List<SubjectConditionEntity> conditionList) {
        this.subjectEntity = subjectEntity;
        this.condition = conditionList.stream()
                .filter(condition -> condition.toId == subjectEntity.id)
                .map(condition -> condition.fromId)
                .collect(Collectors.toList());
    }
}

