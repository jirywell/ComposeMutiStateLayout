## 前言
在Android页面开发中经常遇到多中状态布局切换的情况,常有`加载中`，`加载失败`，`加载为空`，`加载成功`等状态.基于xml封装的例子已经很多了,随着谷歌对compose的大力推广
该项目主要介绍`Compose`如何封装一个简单易用的`MutiStateLayout`,有兴趣的同学可以点个`Star`:[Compose版MutiStateLayout](https://github.com/jirywell/ComposeMutiStateLayout)


## 特性
1. 提供全局默认布局,如默认加载中，默认成功失败
2. 支持自定义默认样式文案，图片
3. 支持完全自定义样式，如果不想使用框架默认提供的布局,可自定义布局样式
4. 支持自定义处理点击重试事件
5. 完全使用数据驱动

## 使用
### 接入
下载项目工程,将ComposeMultiStateLayout.kt拷贝到项目中

### 简单使用
#### 定义全局样式
在框架中没有指定任何默认样式，因此你需要自定义自己的默认加载中，加载失败等页面样式

同时需要自定义传给自定义样式的数据结构类型，方便数据驱动
```kotlin
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
```
如上所示,初始化时我们主要需要做以下事
1. 自定义默认加载中，加载失败，加载为空等样式
2. 自定义`PageState`，即传给默认样式的数据结构，比如文案，图片等，这样后续需要修改的时候只需修改`PageState`即可

#### 直接使用
如果我们直接使用默认样式，直接如下使用即可
```kotlin
   var pageState: PageState by remember {
        mutableStateOf(PageState.Content)
    }
    Column(modifier = Modifier.fillMaxSize()) {
       
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
            })
    }
```
如上所示，可以直接使用，如果需要修改状态，修改`pageStateData`即可

#### 切换布局状态和自定义文案
如果我们需要切换布局状态和自定义文案或者图片等细节，可简单直接修改`pageState`即可,不设置则使用默认文案,图片
```kotlin

    var pageState: PageState by remember {
        mutableStateOf(PageState.Content)
    }
   ButtonSimple(onClick = {
                pageState = PageState.Error.apply {
                    tipTex = "哎呀,出错了"
                    tipImg = R.mipmap.ic_launcher
                }
            }, "Error")

```

#### 自定义布局
有时页面的加载中样式与全局的并不一样，这就需要自定义布局样式了
```kotlin
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
            }, loading = {
                //写loading布局
            }, error = {
                //写error布局
            }, empty = {
                //empty布局

            }, custom = {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "自定义页面(如提示登录)",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.h3
                    )
                }
            })
```
#### 自定义布局中重新加载
```kotlin
error = {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "重新加载",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clickable {
                                it.reload.invoke()
                            },
                        style = MaterialTheme.typography.h3
                    )
                }
            }
            
```

其实代码非常简单，和`Scaffold`脚手架原理很相识,我们可以快速的定义一个包含多状态控件的页面

**邮箱**：664209769@qq.com

## License

    Copyright 2022 赵继瑞

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
