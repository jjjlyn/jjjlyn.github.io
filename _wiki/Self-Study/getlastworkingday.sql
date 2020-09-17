create
    definer = xttm2009@`%` function uf_get_last_working_day(p_day date, p_diff int, p_chk_holiday tinyint(1)) returns date
BEGIN 
	DECLARE v_day DATE;
	DECLARE v_diff INT; -- 며칠 전부터 검색할 것인지 : 나는 2일로 설정 (2일 전)
	DECLARE v_loop_cnt INT;
	DECLARE v_cnt INT DEFAULT 0;
	DECLARE v_cnt_holiday INT DEFAULT 0;

	IF (p_day IS NULL) THEN
		SET v_day = CURDATE();
	ELSE
		SET v_day = p_day;
	END IF;

	SET v_diff = IFNULL(p_diff, 0);
	SET v_loop_cnt = v_diff + 1;

	chk_loop: LOOP
        IF(DAYOFWEEK(v_day) NOT IN (1, 7)) THEN -- 토요일, 일요일에 속하지 않을 때
            IF(p_chk_holiday) THEN -- 휴일일 경우를 체크한다. (1로 설정했을 경우)
                SELECT COUNT(1) -- 휴일일 경우 v_cnt_holiday = 1
                INTO v_cnt_holiday
                FROM holiday
                WHERE 1=1
                AND holiday = v_day
                AND use_yn = 'Y'
                ;

                IF(v_cnt_holiday = 0) THEN -- 휴일이 아닐 경우 (주말도 아닌 경우)
                    SET v_cnt = v_cnt + 1; -- v_cnt를 1 증가시킨다.
                END IF;
            ELSE -- 휴일 여부 체크를 하지 않았을 경우 (주말 여부만 체크할 경우) 그런데 주말도 아닌 경우
                SET v_cnt = v_cnt + 1; -- v_cnt를 1 증가시킨다. 
            END IF;
        END IF;

        IF(v_cnt = v_loop_cnt) THEN
            LEAVE chk_loop;
        ELSE
            -- 특정 일 기준일에 입력된 날짜만큼을 더한다 (하루 씩 전날로 이동)
            SET v_day = DATE_ADD(v_day, INTERVAL -1 DAY);
        END IF;
    END LOOP chk_loop;

    RETURN v_day;
END;