# spring-batch-file
Spring Batch import file to db

前言：项目中需要使用批量文件导入的功能，调研了Spring Batch对应的用法。本文介绍了Spring Batch的基本概念和示例用法，读取平面文件，对数据进行处理后保存至数据库中。更多Spring Batch的介绍和用法请参考官网：

https://spring.io/projects/spring-batch

https://docs.spring.io/spring-batch/docs/4.2.1.RELEASE/api

问题记录

1、在processor中，如果要调用时传进来的参数，例如“createTime”，需要通过JobParameters 来传值和获取。详细见4.3数据处理Processor和4.5调用Job

2、如果文件中出现空行，在processor中进行判断，如果读取的对象属性全为null，直接return null即可。

3、在ItemWriter中，注意sql的写法，以及参数的占位符，需要用“:属性名”。

4、在配置tokenizer时，文件有定长和分隔符的区分，如果是定长文件，需要计算每个列的区间Range，字段较多时，可以使用Excel表格辅助计算。

5、定长文件的tokenizer，如果字段较多，但是只需要读取其中部分，例如一行里有30个字段，但是只读取其中10个字段，其他字段忽略不需要读取，需要设置 tokenizer.setStrict(false)。（一个小坑，最后通过官网的api解决）


CSDN地址：
https://blog.csdn.net/a_newdriver/article/details/136599130
