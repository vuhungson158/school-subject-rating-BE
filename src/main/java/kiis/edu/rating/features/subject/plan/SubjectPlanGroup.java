package kiis.edu.rating.features.subject.plan;

import kiis.edu.rating.enums.Department;
import kiis.edu.rating.features.subject.base.SubjectEntity;
import kiis.edu.rating.features.subject.condition.SubjectConditionEntity;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static kiis.edu.rating.enums.Department.ALL;
import static kiis.edu.rating.enums.Department.values;
import static kiis.edu.rating.enums.SubjectClassification.*;

@AllArgsConstructor
public class SubjectPlanGroup {

    public static List<DepartmentGroup> grouping
            (List<SubjectEntity> subjectEntityList,
             List<SubjectConditionEntity> conditionList) {

        final List<DepartmentGroup> result = new ArrayList<>();
        for (Department department : values()) {
            if (department.equals(ALL)) continue;
            final List<SubjectEntity> subjectsByDepartment = filter(subjectEntityList,
                    subject -> subject.department.equals(department)
                            || subject.department.equals(ALL));
            final List<BigGroup> bigGroupList = new ArrayList<>();
            for (Big big : Big.values()) {
                final List<SubjectEntity> subjectsByBig = filter(subjectsByDepartment,
                        subject -> subject.classification.getMiddle().getBig().equals(big));
                final List<MiddleGroup> middleGroupList = new ArrayList<>();
                int bigRowspan = 0;
                for (Middle middle : Middle.values()) {
                    if (!middle.getBig().equals(big)) continue;
                    final List<SubjectEntity> subjectsByMiddle = filter(subjectsByBig,
                            subject -> subject.classification.getMiddle().equals(middle));
                    final List<SmallGroup> smallGroupList = new ArrayList<>();
                    int middleRowspan = 0;
                    for (Small small : Small.values()) {
                        if (!small.getMiddle().equals(middle)) continue;
                        if (!small.getDepartment().equals(department)
                                && !small.getDepartment().equals(ALL)) continue;
                        middleRowspan++;
                        final List<SubjectEntity> subjectsBySmall = filter(subjectsByMiddle,
                                subject -> subject.classification.equals(small));
                        final SmallGroup smallGroup = new SmallGroup(small, subjectsBySmall, conditionList);
                        smallGroupList.add(smallGroup);
                    }
                    bigRowspan += middleRowspan;
                    final MiddleGroup middleGroup = new MiddleGroup(middle, smallGroupList, middleRowspan);
                    middleGroupList.add(middleGroup);
                }
                final BigGroup bigGroup = new BigGroup(big, middleGroupList, bigRowspan);
                bigGroupList.add(bigGroup);
            }
            final DepartmentGroup departmentGroup = new DepartmentGroup(department, bigGroupList);
            result.add(departmentGroup);
        }
        return result;
    }

    private static List<SubjectEntity> filter(List<SubjectEntity> subjectEntityList, Predicate<SubjectEntity> predicate) {
        return subjectEntityList.stream().filter(predicate).collect(Collectors.toList());
    }

    public static class DepartmentGroup extends Group<Department> {
        public final List<BigGroup> bigList;

        public DepartmentGroup(Department department, List<BigGroup> bigList) {
            super(department, department.getLabel());
            this.bigList = bigList;
        }
    }

    public static class BigGroup extends Group<Big> {
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

    public static class MiddleGroup extends Group<Middle> {
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

    public static class SmallGroup extends Group<Small> {
        public final List<List<SubjectWithCondition>> yearList;

        public SmallGroup(Small small, List<SubjectEntity> subjectEntityList,
                          List<SubjectConditionEntity> conditionList) {
            super(small, small.getLabel());

            List<List<SubjectWithCondition>> yearList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                final int year = i;
                List<SubjectWithCondition> subjectListByYear = subjectEntityList.stream()
                        .filter(subject -> subject.formYear == year)
                        .map(subject -> new SubjectWithCondition(subject, conditionList))
                        .collect(Collectors.toList());
                yearList.add(subjectListByYear);
            }
            this.yearList = yearList;
        }
    }

    @AllArgsConstructor
    public static class Group<N> {
        public final N name;
        public final String label;
    }

    public static class SubjectWithCondition {
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
}

