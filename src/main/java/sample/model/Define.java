package sample.model;

public class Define {

    /** サーバー側で問題が発生しました。 */
    public static final String Exception = "error.Exception";
    /** 情報が見つかりませんでした。 */
    public static final String EntityNotFound = "error.EntityNotFoundException";
    /** 対象機能のアクセスが認められていません。 */
    public static final String AccessDenied = "error.AccessDeniedException";

    /**
     * 従業員エンティティの main_organization キーを定義します。
     *
     * @see sample.model.entity.Employee
     */
    public static class MainOrganization {

        public static final String KEY_DEPARTMENT_CODE = "department_code";
        public static final String KEY_SECTION_CODE = "section_code";
        public static final String KEY_GROUP_CODE = "group_code";
    }

}
