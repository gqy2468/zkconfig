# zkconfig
zkconfig是为zookeeper开发的配置服务工具包，能与现有的Java系统进行良好的集成，也可以使用与非java系统以独立进程运行。
目前支持所有种类配置文件的同步更新，仅支持扩展名为.properties与.cfg结尾的健值对文件格式的内存数据对象实时同步。其余配置文件仅支持磁盘数据同步。
本代码是在QuanZhong/ZKConfig（ https://github.com/QuanZhong/ZKConfig ）的代码基础上改进而来，增加了单文件同步更新以及新增文件自动加入监控。
