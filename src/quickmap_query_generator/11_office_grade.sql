set define off;

insert all
into qm_office_grade (grade_id, grade_name)values (0, '일반')
into qm_office_grade (grade_id, grade_name)values (1, '프리미엄')
select * from dual;

commit;

-- total row    : 2
-- max name len : 4
