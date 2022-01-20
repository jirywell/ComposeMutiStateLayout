package com.rui.statelayout

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


sealed class PageState(
    var tipTex: String,
    var tipImg: Int
) {
    object Loading : PageState(
        tipTex = "加载中",
        tipImg = R.mipmap.ic_launcher
    )

    object Empty : PageState(
        tipTex = "数据为空",
        tipImg = R.mipmap.ic_launcher
    )

    object Error : PageState(
        tipTex = "数据出错",
        tipImg = R.mipmap.ic_launcher
    )

    object Custom : PageState("", 0)
    object Content : PageState("", 0)
}


data class LayoutData(val pageStateData: PageState, val reload: () -> Unit = {})

@Composable
fun ComposeMultiStateStateLayout(
    modifier: Modifier = Modifier,
    pageState: PageState,
    onReLoad: () -> Unit = { },
    loading: @Composable (LayoutData) -> Unit = {},
    empty: @Composable (LayoutData) -> Unit = {},
    error: @Composable (LayoutData) -> Unit = {},
    custom: @Composable (LayoutData) -> Unit = {},
    content: @Composable () -> Unit = { }
) {

    val stateLayoutData = LayoutData(pageState, onReLoad)
    Box(modifier = modifier) {
        when (pageState) {
            PageState.Loading -> loading(stateLayoutData)
            PageState.Empty -> empty(stateLayoutData)
            PageState.Error -> error(stateLayoutData)
            PageState.Custom -> custom(stateLayoutData)
            PageState.Content -> content()

        }
    }
}

@Composable
fun DefaultMultiStateLayout(
    modifier: Modifier = Modifier,
    pageState: PageState,
    onReLoad: () -> Unit = { },
    loading: @Composable (LayoutData) -> Unit = { DefaultLoadingLayout(it) },
    empty: @Composable (LayoutData) -> Unit = { DefaultEmptyLayout(it) },
    error: @Composable (LayoutData) -> Unit = { DefaultErrorLayout(it) },
    custom: @Composable (LayoutData) -> Unit = {},
    content: @Composable () -> Unit = { }
) {
    ComposeMultiStateStateLayout(
        modifier = modifier,
        pageState = pageState,
        onReLoad = onReLoad,
        loading = { loading(it) },
        empty = { empty(it) },
        error = { error(it) },
        custom = { custom(it) },
        content = content
    )
}

@Composable
fun DefaultLoadingLayout(layoutData: LayoutData) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Text(
                text = layoutData.pageStateData.tipTex,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(16.dp)
            )
        }
    }


}

@Composable
fun DefaultEmptyLayout(layoutData: LayoutData) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .clickable {
                    layoutData.reload()
                }
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    id = layoutData.pageStateData.tipImg
                ),
                modifier = Modifier.size(200.dp),
                contentDescription = ""
            )
            Text(
                text = layoutData.pageStateData.tipTex,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(16.dp)
            )
        }
    }


}

@Composable
fun DefaultErrorLayout(layoutData: LayoutData) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
                .clickable {
                    layoutData.reload()
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = layoutData.pageStateData.tipImg),
                modifier = Modifier.size(200.dp),
                contentDescription = ""
            )
            Text(
                text = layoutData.pageStateData.tipTex,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .padding(16.dp, 0.dp)
            )

        }
    }

}