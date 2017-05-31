package com.example.przemek.egzaminel.Network;


public class AppConfig {

    static final String DEFAULT_ENCODING = "UTF-8";
    static final String DEFAULT_ISO_STANDARD = "ISO-8859-1";
    static final String BASADATE_DATETIME_REG = "yyyy-MM-dd HH:mm:ss";


    public static final String URL_ADDRESS = "http://serwer1757813.home.pl";
    public static final String EXAMS_BY_GROUP = URL_ADDRESS + "/egzaminelAPI/exams_by_group_id.php";
    public static final String GROUPS_BY_USER_ID = URL_ADDRESS +  "/egzaminelAPI/groups_by_user_id.php";
    public static final String TERMS_BY_EXAM_ID = URL_ADDRESS +  "/egzaminelAPI/terms_by_exam_id.php";
    public static final String EXAMS_BY_USER_ID = URL_ADDRESS +  "/egzaminelAPI/exams_by_user_id.php";
    public static final String TERMS_BY_USER_ID = URL_ADDRESS +  "/egzaminelAPI/terms_by_user_id.php";

    public static final String GROUP_BY_GROUP_ID_AND_PASSWORD = URL_ADDRESS + "/egzaminelAPI/group_by_group_id_and_password.php";
    public static final String EXAMS_AND_TERMS_BY_GROUPS_IDS = URL_ADDRESS + "/egzaminelAPI/exams_and_terms_by_groups_ids.php";

    //log in//192.168.0.103/
    public static final String LOG_IN_USERNAME = "username";
    public static final String LOG_IN_PASSWORD = "password";
    public static final String LOG_IN_ID = "id";



    public static final String EMPTY_ANSWER = "[]";
    public static final String DATE_DEFAULT = "1970-01-01 00:00:00";

    public static final String USER_ID = "user_id";
    public static final String GROUP_ID = "group_id";
    public static final String GROUPS_IDS = "groups_ids";
    public static final String GROUP_PASSWORD = "password";
    public static final String EXAM_ID = "exam_id";

    public static final String ERROR_CODE = "error";
    public static final String RESULT_CODE = "result";
    public static final String RESULT_CODE_MUL_EXAMS = "result_exams";
    public static final String RESULT_CODE_MUL_TERMS = "result_terms";

    //ID
    public static final String ID = "id";

    //groups
    public static final String GROUP_NAME = "name";
    public static final String GROUP_DESCRIPTION = "description";

    //exam
    public static final String EXAM_GROUP_ID = "group_id";
    public static final String EXAM_SUBJECT = "subject";
    public static final String EXAM_TYPE = "type";
    public static final String EXAM_DESCRIPTION = "description";
    public static final String EXAM_TEACHER = "teacher";
    public static final String EXAM_MATERIALS = "materials";
    public static final String EXAM_LAST_UPDATE = "last_update";

    //term
    public static final String TERM_EXAM_ID = "exam_id";
    public static final String TERM_DATE = "date";
    public static final String TERM_PLACE = "place";

}