set define off;

insert all
into qm_service (service_id, service_name)values (1, '내과')
into qm_service (service_id, service_name)values (10, '성형외과')
into qm_service (service_id, service_name)values (11, '신경외과')
into qm_service (service_id, service_name)values (12, '정형외과')
into qm_service (service_id, service_name)values (13, '흉부외과')
into qm_service (service_id, service_name)values (14, '외과')
into qm_service (service_id, service_name)values (30, '소아청소년과')
into qm_service (service_id, service_name)values (31, '산부인과')
into qm_service (service_id, service_name)values (32, '피부과')
into qm_service (service_id, service_name)values (33, '비뇨기과')
into qm_service (service_id, service_name)values (34, '안과')
into qm_service (service_id, service_name)values (35, '이비인후과')
into qm_service (service_id, service_name)values (36, '정신과')
into qm_service (service_id, service_name)values (37, '마취통증의학과')
into qm_service (service_id, service_name)values (38, '가정의학과')
into qm_service (service_id, service_name)values (39, '재활의학과')
into qm_service (service_id, service_name)values (40, '영상의학과')
into qm_service (service_id, service_name)values (41, '병리과')
into qm_service (service_id, service_name)values (50, '치과')
into qm_service (service_id, service_name)values (60, '한방과')
into qm_service (service_id, service_name)values (99, '약국')
select * from dual;

commit;

-- total row    : 21
-- max name len : 7
