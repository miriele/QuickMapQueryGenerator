set define off;

insert all
into qm_user_grade (grade_id, grade_name)values (0, 'Ż��')
into qm_user_grade (grade_id, grade_name)values (1, 'Ȱ������')
into qm_user_grade (grade_id, grade_name)values (2, '�Ϲ�')
into qm_user_grade (grade_id, grade_name)values (3, '���')
into qm_user_grade (grade_id, grade_name)values (9, '������')
select * from dual;

commit;

-- total row    : 5
-- max name len : 4
