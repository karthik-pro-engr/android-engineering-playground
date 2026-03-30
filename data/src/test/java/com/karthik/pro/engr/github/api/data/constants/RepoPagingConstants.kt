package com.karthik.pro.engr.github.api.data.constants

object RepoPagingConstants {
     const val SUCCESS_PAGE1_RESPONSE_PATH = "repo/success/repo_page1_success.json"
     const val SUCCESS_LAST_PAGE_RESPONSE_PATH =
        "repo/success/repo_last_page_success.json"
     const val INTERNAL_SERVER_ERROR_PATH = "repo/error/internal_server_error.json"
     const val USER_NOT_FOUND_ERROR_PATH = "repo/error/user_not_found_error.json"

     const val FIRST_PAGE_LINK_HEADER =
        "<https://api.github.com/user/66577/repos?per_page=8&page=2>; rel=\"next\", <https://api.github.com/user/66577/repos?per_page=8&page=19>; rel=\"last\""
     const val LAST_PAGE_LINK_HEADER =
        "<https://api.github.com/user/66577/repos?per_page=8&page=18>; rel=\"prev\", <https://api.github.com/user/66577/repos?per_page=8&page=1>; rel=\"first\""

     const val SECOND_PAGE_LINK_HEADER =
        "<https://api.github.com/user/66577/repos?per_page=8&page=1>; rel=\"prev\"," +
                "<https://api.github.com/user/66577/repos?per_page=8&page=3>; rel=\"next\"," +
                "<https://api.github.com/user/66577/repos?per_page=8&page=1>; rel=\"first\"," +
                "<https://api.github.com/user/66577/repos?per_page=8&page=19>; rel=\"last\""

     const val USERNAME = "JakeWharton"
     const val LINK_TAG = "Link"
     const val NOT_FOUND = "Not Found"
     const val SERVER_ERROR = "Server Error"

     const val MALFORMED_JSON = """[{ "id": 1, "name": """
     const val DEFAULT_PAGE_SIZE = 8
}