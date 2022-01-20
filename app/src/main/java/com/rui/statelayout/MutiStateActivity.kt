package com.rui.statelayout

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rui.statelayout.ui.theme.ComposeMutiStateLayoutTheme

class MutiStateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMutiStateLayoutTheme {
                TestLayout()
            }
        }
    }
}

@Composable
fun TestLayout() {
    var pageState: PageState by remember {
        mutableStateOf(PageState.Content)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ButtonSimple(onClick = { pageState = PageState.Content }, "Content")
            ButtonSimple(onClick = {
                pageState = PageState.Loading.apply {
                    tipTex = "加载中,请稍后...."
                }
            }, "Loading")
            ButtonSimple(onClick = { pageState = PageState.Empty }, "Empty")

            ButtonSimple(onClick = {
                pageState = PageState.Error.apply {
                    tipTex = "哎呀,出错了"
                    tipImg = R.mipmap.ic_launcher
                }
            }, "Error")
            ButtonSimple(onClick = { pageState = PageState.Custom }, "Custom")
        }
        DefaultMultiStateLayout(modifier = Modifier.fillMaxSize(),
            pageState = pageState,
            onReLoad = {
                pageState = PageState.Loading
            },
            content = {
                Box(modifier = Modifier.fillMaxSize()) {

                    Text(
                        text = "内容页面",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.h3
                    )
                }
            }, custom = {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "自定义页面",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.h3
                    )
                }
            })
    }
}

@Composable
fun ButtonSimple(onClick: () -> Unit, text: String) {
    Button(onClick = { onClick.invoke() }) {
        Text(text = text, style = MaterialTheme.typography.caption)
    }
}

@Preview
@Composable
fun TestLayoutPreview() {
    ComposeMutiStateLayoutTheme {
        TestLayout()
    }
}