set define off;

insert all
into qm_user_grade (grade_id, grade_name)values (0, '탈퇴')
into qm_user_grade (grade_id, grade_name)values (1, '활동정지')
into qm_user_grade (grade_id, grade_name)values (2, '일반')
into qm_user_grade (grade_id, grade_name)values (3, '등록')
into qm_user_grade (grade_id, grade_name)values (9, '관리자')
select * from dual;

commit;

-- total row    : 5
-- max name len : 4
