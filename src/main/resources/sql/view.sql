create view statistic_view
as
select 1 as id,
       (select count(role = 'USER' or null) from auth_user limit 1)                   as total_user,
       (select count(role = 'MANAGER' or null) from auth_user limit 1)                as total_manager,
       (select count(role = 'ADMIN' or null) from auth_user limit 1)                  as total_admin,
       (select count(disable = false or null) from subject limit 1)                   as total_subject,
       (select count(disable = false or null) from subject_comment limit 1)           as total_subject_comment,
       (select count(disable = false or null) from subject_comment_react scr limit 1) as total_subject_comment_react,
       (select count(disable = false or null) from subject_rating sr limit 1)         as total_subject_rating,
       (select count(teacher.disable = false or null) from teacher limit 1)           as total_teacher,
       (select count(disable = false or null) from teacher_comment limit 1)           as total_teacher_comment,
       (select count(disable = false or null) from teacher_comment_react limit 1)     as total_teacher_comment_react,
       (select count(disable = false or null) from teacher_rating limit 1)            as total_teacher_rating
from auth_user
where true
limit 1;

