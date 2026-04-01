import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.karthik.pro.engr.github.api.domain.constants.PaginationConstants.DEFAULT_PAGE_SIZE
import com.karthik.pro.engr.github.api.domain.model.Owner
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.playground.presentation.handlers.PagingScreenHandler
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.APPEND_ERROR
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.APPEND_LOADER
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.FULL_SCREEN_ERROR
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.FULL_SCREEN_LOADER
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.REPO_ITEM
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.REPO_LIST
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.RETRY_BUTTON
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.SEARCH_BUTTON
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.SEARCH_INPUT
import com.karthik.pro.engr.github.api.playground.presentation.repos.RepoListItem
import com.karthik.pro.engr.github.api.playground.presentation.repos.ReposSearchByUserName
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class RepoListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun ComposeContentTestRule.launchPagingScreen(
        flow: Flow<PagingData<Repo>>
    ) {
        setContent {
            val lazyPagingItems = flow.collectAsLazyPagingItems()
            PagingScreenHandler(
                lazyPagingItems = lazyPagingItems,
                emptyContent = {},
                itemContent = { value -> RepoListItem(repo = value) })
        }
    }

    private fun createPager(
        pagingSourceFactory: () -> PagingSource<Int, Repo>
    ): Flow<PagingData<Repo>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }


    @Test
    fun search_input_and_click_triggers_onSubmit() {
        var submittedText: String? = null

        composeTestRule.setContent {
            ReposSearchByUserName(
                currentQuery = null,
                onSubmit = { value ->
                    submittedText = value
                }

            )
        }

        composeTestRule.onNodeWithTag(SEARCH_INPUT)
            .performTextInput("JakeWharton")

        composeTestRule.onNodeWithTag(SEARCH_BUTTON)
            .performClick()

        assertThat(submittedText).isEqualTo("JakeWharton")

    }

    @Test
    fun show_full_screen_loader_when_refresh_state() {

        val flow = flow<PagingData<Repo>> {
            // No emission
        }

        composeTestRule.launchPagingScreen(flow)

        composeTestRule.onNodeWithTag(FULL_SCREEN_LOADER)
            .assertIsDisplayed()

    }

    @Test
    fun show_full_screen_error_when_error_state() {
        val flow = createPager { ErrorPagingSource() }

        composeTestRule.launchPagingScreen(flow)

        composeTestRule.onNodeWithTag(FULL_SCREEN_ERROR)
            .assertIsDisplayed()

    }

    @Test
    fun pagingScreenHandler_callsOnRetry_whenRetryClicked() {
        var retryCalled = false
        val flow = createPager { ErrorPagingSource() }

        composeTestRule.setContent {
            val lazyPagingItems = flow.collectAsLazyPagingItems()
            PagingScreenHandler(
                lazyPagingItems = lazyPagingItems,
                onRetry = { retryCalled = true },
                emptyContent = {},
                itemContent = { value -> RepoListItem(repo = value) })
        }

        composeTestRule.onNodeWithTag(RETRY_BUTTON)
            .performClick()

        assertThat(retryCalled).isTrue()

    }

    @Test
    fun shows_append_loader_when_scrolled_to_end() {
        val gate = CompletableDeferred<Unit>()
        val flow = createPager { TestPagingSource(gate) }

        composeTestRule.launchPagingScreen(flow)

        composeTestRule.waitUntil()

        composeTestRule
            .onNodeWithTag(REPO_LIST)
            .performScrollToIndex(7)

        composeTestRule
            .onNodeWithTag(APPEND_LOADER)
            .assertIsDisplayed()

    }

    @Test
    fun shows_append_retry_when_append_page_is_error() {
        val gate = CompletableDeferred<Unit>()
        val flow = createPager { TestPagingSource(gate, AppendState.ERROR) }

        composeTestRule.launchPagingScreen(flow)

        composeTestRule.waitUntil()

        composeTestRule
            .onNodeWithTag(REPO_LIST)
            .performScrollToIndex(7)

        composeTestRule
            .onNodeWithTag(APPEND_LOADER)
            .assertIsDisplayed()

        gate.complete(Unit)

        composeTestRule
            .onNodeWithTag(APPEND_ERROR)
            .assertIsDisplayed()

    }

    @Test
    fun append_error_then_retry_shows_success_items() {
        val gate = CompletableDeferred<Unit>()

        val testPagingSource = TestPagingSource(gate, AppendState.ERROR)

        val flow = createPager {
            testPagingSource
        }

        composeTestRule.launchPagingScreen(flow)

        composeTestRule.waitUntil()

        composeTestRule
            .onNodeWithTag(REPO_LIST)
            .performScrollToIndex(7)

        composeTestRule
            .onNodeWithTag(APPEND_LOADER)
            .assertIsDisplayed()

        gate.complete(Unit)

        composeTestRule
            .onNodeWithTag(APPEND_ERROR)
            .assertIsDisplayed()

        testPagingSource.switchToSuccess()


        composeTestRule
            .onNodeWithTag(RETRY_BUTTON)
            .performClick()

        composeTestRule.waitUntil {
            composeTestRule
                .onAllNodesWithTag(REPO_ITEM)
                .fetchSemanticsNodes().size > 10
        }

        composeTestRule
            .onAllNodesWithTag(APPEND_ERROR)
            .assertCountEquals(0)

    }

    private fun ComposeContentTestRule.waitUntil() {
        waitUntil {
            onAllNodesWithTag(
                REPO_ITEM
            ).fetchSemanticsNodes().isNotEmpty()
        }
    }


    class TestPagingSource(
        private val gate: CompletableDeferred<Unit>,
        private var appendState: AppendState = AppendState.LOADING
    ) : PagingSource<Int, Repo>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
            val page = params.key ?: 1
            return when (page) {
                1 -> {
                    LoadResult.Page(
                        data = repos(), prevKey = null,
                        nextKey = 2
                    )
                }

                2 -> {
                    gate.await()
                    when (appendState) {
                        AppendState.LOADING -> {
                            LoadResult.Page(emptyList(), null, null)
                        }

                        AppendState.ERROR -> {
                            LoadResult.Error(IOException("Append failed"))

                        }

                        AppendState.SUCCESS -> {
                            LoadResult.Page(repos(), null, null)
                        }
                    }


                }

                else -> LoadResult.Page(emptyList(), null, null)
            }
        }

        private fun repos(): List<Repo> = List(10) {
            Repo(
                id = Random.nextLong(),
                name = "admin-tools",
                fullName = "karthik-pro-engr/admin-tools",
                description = "",
                htmlUrl = "https://github.com/karthik-pro-engr",
                language = "Shell",
                stars = 0,
                forks = 0,
                owner = Owner(
                    login = "karthik-pro-engr",
                    id = 101930095,
                    avatarUrl = "https://avatars.githubusercontent.com/u/101930095?v=4",
                    htmlUrl = "https://github.com/karthik-pro-engr"
                )
            )

        }

        override fun getRefreshKey(state: PagingState<Int, Repo>): Int? = null


        fun switchToSuccess() {
            appendState = AppendState.SUCCESS
        }

    }

    class ErrorPagingSource : PagingSource<Int, Repo>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
            return LoadResult.Error(Exception("Test error"))
        }

        override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
            return null
        }

    }

    enum class AppendState {
        LOADING,
        ERROR,
        SUCCESS
    }
}