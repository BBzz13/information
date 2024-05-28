## 1、装卸旧版本docker
```linux
sudo yum remove docker \
                   docker-client \
                   docker-client-latest \
                   docker-common \
                   docker-latest \
                   docker-latest-logrotate \
                   docker-logrotate \
                   docker-engine
```
![在这里插入图片描述](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/7c4d8c465e5e4405a0f272241f3d6458~tplv-k3u1fbpfcp-zoom-1.image)

## 2、安装gcc
```linux
yum -y install gcc
yum -y install gcc-c++
```
![yum -y install gcc](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/03b23cb05751461bb228c8b15d929d17~tplv-k3u1fbpfcp-zoom-1.image)
![yum -y install gcc-c++](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a8cb8a9a0a444c44b184fdd2e4e257f0~tplv-k3u1fbpfcp-zoom-1.image)

此时可能会出现报错如下
Errors during downloading metadata for repository 'AppStream':- Status code: 404 for http://mirrors.cloud.aliyuncs.com/centos/8/AppStream/x86_64/os/repodata/repomd.xml (IP: 100.100.2.148)
Error: Failed to download metadata for repo 'AppStream': Cannot download repomd.xml: Cannot download repodata/repomd.xml: All mirrors were tried
**问题原因：由于centos8官方已经停止提供服务，相应的yum源也已经移到归档源**
**解决办法：**
1、运行以下命令备份之前的repo文件。
```linux 
rename '.repo' '.repo.bak' /etc/yum.repos.d/*.repo
```
2、运行以下命令下载最新的repo文件
```linux 
wget https://mirrors.aliyun.com/repo/Centos-vault-8.5.2111.repo -O /etc/yum.repos.d/Centos-vault-8.5.2111.repo wget https://mirrors.aliyun.com/repo/epel-archive-8.repo -O /etc/yum.repos.d/epel-archive-8.repo
```
3、运行以下命令替换repo文件中的链接
```linux 
sed -i 's/mirrors.cloud.aliyuncs.com/url_tmp/g' /etc/yum.repos.d/Centos-vault-8.5.2111.repo && sed -i 's/mirrors.aliyun.com/mirrors.cloud.aliyuncs.com/g' /etc/yum.repos.d/Centos-vault-8.5.2111.repo && sed -i 's/url_tmp/mirrors.aliyun.com/g' /etc/yum.repos.d/Centos-vault-8.5.2111.repo sed -i 's/mirrors.aliyun.com/mirrors.cloud.aliyuncs.com/g' /etc/yum.repos.d/epel-archive-8.repo
```
4、运行以下命令重新创建缓存
```Linux
yum clean all
yum makecache
```
**重新执行第二步**
## 3、安装yum-utils和stable repository
```linux
yum install -y yum-utils
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

## 4、安装最新版本的 Docker Engine 和 containerd
```linux
yum install docker-ce docker-ce-cli containerd.io
```
![在这里插入图片描述](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/39a0cc33d7c34a108d52489c2609b4d2~tplv-k3u1fbpfcp-zoom-1.image)
## 6、启动docker
```linux
systemctl start docker
```
开机自启docker : systemctl enable docker
## 7、查看docker版本信息
```linux
docker version
```
![在这里插入图片描述](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/22a6a1270506426082f35c318c58a49a~tplv-k3u1fbpfcp-zoom-1.image)
## 8、卸载
```linux
systemctl stop docker
yum remove docker-ce docker-ce-cli containerd.io
rm -rf /var/lib/docker
rm -rf /var/lib/containerd
```