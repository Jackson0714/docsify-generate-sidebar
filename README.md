# docsify-generate-sidebar
一键生成 docsify 的侧边栏

## 背景

docsify 网站的侧边栏每次都需要手动添加菜单链接，比较麻烦，想要一键自动生成目录。比如下图框起来的目录。

![](http://cdn.jayh.club/uPic/image-20240906220729425uKA0ZB.png)

当我们需要修改侧边栏菜单时，需要编辑 _sidebar.md 文件才行。

![image-20240906220823739](http://cdn.jayh.club/uPic/image-20240906220823739AyE9ic.png)

该文件的内容如下：

![](http://cdn.jayh.club/uPic/image-20240906220931026yaVvFF.png)

那有没有一种办法能自动添加侧边栏目录，或者一键生成侧边栏目录呢？

我写了一个小工具，一键生成docsify 的目录，github 上开源了。

## 包含功能

- 一键生成docsify 的目录文件 _sidebar.md 和 README.md 文件。
- 上传修改的文件到 Git 仓库。
- 服务器拉取最新仓库代码，更新网站菜单。

## 前提

本地网站代码需要配置远程的代码仓库地址和分支。提交命令在代码里面，如下所示：

``` SH
git push origin main
```

## 代码

[docsify-generate-sidebar](https://github.com/Jackson0714/docsify-generate-sidebar)