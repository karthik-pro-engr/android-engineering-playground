import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.karthik.pro.engr.github.api.core.testing.RepoFactory
import com.karthik.pro.engr.github.api.domain.constants.PaginationConstants.DEFAULT_PAGE_SIZE
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.playground.presentation.handlers.PagingScreenHandler
import com.karthik.pro.engr.github.api.playground.presentation.repos.RepoListItem
import com.karthik.pro.engr.github.api.playground.presentation.repos.ReposSearchByUserName
import com.karthik.pro.engr.github.api.playground.repos.robot.repoListRobot
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
                itemContent = { RepoListItem(repo = it){ownerName, repoName-> println("Item clicked")} }
            )
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
    fun search_triggers_submit() = with(composeTestRule) {

        var submittedText: String? = null

        setContent {
            ReposSearchByUserName(
                currentQuery = null,
                onSubmit = { submittedText = it }
            )
        }

        repoListRobot(this) {
            enterUsername("JakeWharton")
            tapSearch()
        }

        assertThat(submittedText).isEqualTo("JakeWharton")
    }

    @Test
    fun show_full_screen_loader_when_refresh_state() = with(composeTestRule) {

        val flow = flow<PagingData<Repo>> { }

        launchPagingScreen(flow)

        repoListRobot(this) {
            assertFullScreenLoaderDisplayed()
        }
    }

    @Test
    fun show_full_screen_error_when_error_state() = with(composeTestRule) {

        val flow = createPager { ErrorPagingSource() }

        launchPagingScreen(flow)

        repoListRobot(this) {
            assertFullScreenErrorDisplayed()
        }
    }

    @Test
    fun append_loading_shows_loader() = with(composeTestRule) {

        val gate = CompletableDeferred<Unit>()
        val flow = createPager { TestPagingSource(gate) }

        launchPagingScreen(flow)

        repoListRobot(this) {
            assertRepoListDisplayed()
            scrollToLoadMore()
            assertAppendLoaderDisplayed()
        }
    }

    @Test
    fun append_error_shows_retry() = with(composeTestRule) {

        val gate = CompletableDeferred<Unit>()
        val flow = createPager {
            TestPagingSource(gate, AppendState.ERROR)
        }

        launchPagingScreen(flow)

        repoListRobot(this) {
            assertRepoListDisplayed()
            scrollToLoadMore()
            assertAppendLoaderDisplayed()
        }

        gate.complete(Unit)

        repoListRobot(this) {
            assertAppendErrorDisplayed()
        }
    }

    @Test
    fun append_error_retry_loads_success_items() = with(composeTestRule) {

        val gate = CompletableDeferred<Unit>()
        val pagingSource = TestPagingSource(gate, AppendState.ERROR)

        val flow = createPager { pagingSource }

        launchPagingScreen(flow)

        repoListRobot(this) {
            assertRepoListDisplayed()
            scrollToLoadMore()
            assertAppendLoaderDisplayed()
        }

        gate.complete(Unit)

        repoListRobot(this) {
            assertAppendErrorDisplayed()
        }

        pagingSource.switchToSuccess()

        repoListRobot(this) {
            tapRetry()
            assertMoreItemsLoaded()
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
            RepoFactory.withId(Random.nextLong())
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