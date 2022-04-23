# docker 部署项目



>   打包成镜像

```shell
docker build -t hairychildren:1.0 .
```



>   运行镜像

```shell
docker run -d -p 9421:9421 --name hairychildren -v /mydata/hairy-children/photo/avatar:/photo/avatar -v /mydata/hairy-children/photo/title:/photo/title -v /mydata/hairy-children/photo/adopt:/photo/adopt -v /mydata/hairy-children/photo/clue:/photo/clue hairychildren:1.0
```

