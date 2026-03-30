package com.karthik.pro.engr.github.api.data.remote.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.karthik.pro.engr.github.api.data.remote.api.GithubService
import com.karthik.pro.engr.github.api.data.remote.constants.NetworkConstants
import com.karthik.pro.engr.github.api.data.remote.constants.NetworkConstants.STARTING_PAGE_INDEX
import com.karthik.pro.engr.github.api.data.remote.error.ApiException
import com.karthik.pro.engr.github.api.data.remote.error.ErrorParser
import com.karthik.pro.engr.github.api.data.remote.mapper.RepoMapper
import com.karthik.pro.engr.github.api.domain.constants.PaginationConstants
import com.karthik.pro.engr.github.api.domain.model.Repo
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class GithubPagingSource @Inject constructor(
    private val service: GithubService,
    private val errorParser: ErrorParser,
    private val username: String,
    private val perPage: Int = PaginationConstants.DEFAULT_PAGE_SIZE
) : PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.listUserRepos(username, perPage, page)
            if (response.isSuccessful) {
                val result = response.body().orEmpty()
                val repos = RepoMapper.fromDtoList(result)
                val headerLink = response.headers()[NetworkConstants.HEADER_LINK]

                val pageLinks =
                    GithubPaginationParser.parsePageNumbers(headerLink)

                LoadResult.Page(
                    data = repos,
                    prevKey = pageLinks.previousKey,
                    nextKey = pageLinks.nextKey
                )

            } else {
                val errorDto = errorParser.parse(response)
                LoadResult.Error(
                    ApiException(
                        message = errorDto.message,
                        code = response.code(),
                        headers = response.headers()
                    )
                )

            }
        } catch (io: IOException) {
            LoadResult.Error(io)
        } catch (t: Throwable) {
            if (t is CancellationException) throw t
            LoadResult.Error(t)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition)

        return page?.nextKey?.minus(1) ?: page?.prevKey?.plus(1)
    }
}