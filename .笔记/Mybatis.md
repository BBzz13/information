# Mybatis注解大全

| **注解** | **目标** | **相对应的 XML** | **描述**|
| ------- | -------- | --------------- | ------- |
| @Arg                | 方法     | `<arg>` `<idArg>`| 单 独 的 构 造 方 法 参 数 , 是 ConstructorArgs 集合的一部分。属性: id,column,javaType,typeHandler。 id 属性是布尔值, 来标识用于比较的属 性,和<idArg>XML 元素相似。 |
| @AutomapConstructor |          |                                 |                                                              |
| @CacheNamespace     |          |                                 |                                                              |
| @CacheNamespaceRef  |          |                                 |                                                              |
| @Case               |          |                                 |                                                              |
| @ConstructorArgs    |          |                                 |                                                              |
| @Flush              |          |                                 |                                                              |
| @Insert   | 方法 | `<insert>`   | 代表执行的真 实 SQL，此注解用来编写插入操作SQL语句。  |
| @Delete  | 方法 | `<update>` | 代表执行的真 实 SQL，此注解用来编写删除操作SQL语句。  |
| @Update | 方法 | `<delete>`  | 代表执行的真 实 SQL，此注解用来编写修改操作SQL语句。  |
| @Select   | 方法 | `<select>`   | 代表执行的真 实 SQL，此注解用来编写查询操作SQL语句。  |
| @InsertProvider     | 方法 |  `<insert>`  | SQL 注解允许你指定一个类名和一个方法在执行时来返回运行允许创建动态 的 SQL。基于执行的映射语句，MyBatis 会实例化这个类，然后执行由provider 指定的方法。该方法可以有选择地接受参数对象。(In MyBatis 3.4 or later, it’s allow multiple parameters) 属性: type,method。type 属性是类。method 属性是方法名。注意: 这节之后是对类的讨论，它可以帮助你以干净，容于阅读 的方式来构建动态 SQL。 |
| @DeleteProvider    | 方法 | `<update>` | SQL 注解允许你指定一个类名和一个方法在执行时来返回运行允许创建动态 的 SQL。基于执行的映射语句，MyBatis 会实例化这个类，然后执行由provider 指定的方法。该方法可以有选择地接受参数对象。(In MyBatis 3.4 or later, it’s allow multiple parameters) 属性: type,method。type 属性是类。method 属性是方法名。注意: 这节之后是对类的讨论，它可以帮助你以干净，容于阅读 的方式来构建动态 SQL。 |
| @UpdateProvider   | 方法 | `<delete>`  | SQL 注解允许你指定一个类名和一个方法在执行时来返回运行允许创建动态 的 SQL。基于执行的映射语句，MyBatis 会实例化这个类，然后执行由provider 指定的方法。该方法可以有选择地接受参数对象。(In MyBatis 3.4 or later, it’s allow multiple parameters) 属性: type,method。type 属性是类。method 属性是方法名。注意: 这节之后是对类的讨论，它可以帮助你以干净，容于阅读 的方式来构建动态 SQL。 |
| @SelectProvider     | 方法 | `<select>`   | SQL 注解允许你指定一个类名和一个方法在执行时来返回运行允许创建动态 的 SQL。基于执行的映射语句，MyBatis 会实例化这个类，然后执行由provider 指定的方法。该方法可以有选择地接受参数对象。(In MyBatis 3.4 or later, it’s allow multiple parameters) 属性: type,method。type 属性是类。method 属性是方法名。注意: 这节之后是对类的讨论，它可以帮助你以干净，容于阅读 的方式来构建动态 SQL。 |
| @Lang               |          |                                 |                                                              |
| @Many               |          |                                 |                                                              |
| @MapKey             |          |                                 |                                                              |
| @Mapper             | 类       | `<mapper namespace="映射地址">` | 在接口类上添加了@Mapper，在编译之后会生成相应的接口实现类。使用@Mapper可以不在包扫描而创建dao接口实现类对象。如果再搭配上注解式sql可以实现无mapper.xml。 |
| @One                |          |                                 |                                                              |
| @Options            |          |                                 |                                                              |
| @Param | 参数 | N/A | 如果你的映射器的方法需要多个参数，这个注解可以被应用于映射器的方法参数来给每个参数一个名字。否则，多参数将会以它们的顺序位置来被命名 (不包括任何RowBounds参数) 比如。 #{param1}，#{param2}等，这是默认的。使用@Param(“person”)，参数应该被命名为#{person}。 |
| @Property |          |                                 |                                                              |
| @Result |          |                                 |                                                              |
| @ResultMap |          |                                 |                                                              |
| @Results |          |                                 |                                                              |
| @ResultType |          |                                 |                                                              |
| @SelectKey | 方法 | `<selectKey>` | SelectKey在Mybatis中是为了解决Insert数据时不支持主键自动生成的问题。数据库主键包括自增和非自增，有时候新增一条数据不仅仅知道成功就行了，后边的逻辑可能还需要这个新增的主键，这时候再查询数据库就有点耗时耗力，我们可以采用selectKey来帮助我们获取新增的主键。<br/>属性：keyProperty：表示查询结果赋值给代码中的哪个对象；<br/>keyColumn：表示将查询结果赋值给数据库表中哪一列；<br/>resultType：填入 keyProperty 的 Java 类型；<br/>before：可以设置为 true 或false。要运行的SQL语句。before=true表示插入之前进行查询，可以将查询结果赋给keyProperty和keyColumn，赋给keyColumn相当于更改数据库。before=false表示先插入，再查询，这时只能将结果赋给keyProperty；<br/>statementType：要运行的SQL语句，它的返回值通过resultType来指定； |
| @TypeDiscriminator |          |                                 |                                                              |

## @InsertProvider、@DeleteProvider、@UpdateProvider、@SelectProvider

插入一条user的数据，可以直接根据username和password插入.

**mapper层代码**

```java
//insert into user(username,password) values(?,?)
@Insert("insert into user(username,password) values(#{username},#{password})")
void save(User user);
```

但是如果有一个需求，要求传入username和password能正常输入，传入username、password、id也能够正常插入，这个时候就可以使用Provider注解。这个的用法就是可以创建一个方法类，在类里面做判断，让mapper里面的查询方法根据写的方法来选择需要的sql语句。
**Userprovider类**

```java
import com.sikiedu.springbootssm.entity.User;

public class Userprovider {
    public String saveUser(User user){
        if(user.getId()==null){
            return "insert into user(username,password) values(#{username},#{password}) ";
        } else {
            return "insert into user values(#{id},#{username},#{password})";
        }
    }
}
```
在这个类里面我们做判断，选择需要的sql语句。

**mapper层的书写**

```java
@InsertProvider(type = Userprovider.class,method="saveUser")
void save (User user);
```

**其他此类注解同理**

## @SelectKey

**IUserDao接口：**

```java
    /**
     * 保存用户
     */
    @Insert("insert into user(username,birthday,sex,address) values (#{userName},#{userBirthday},#{userSex},#{userAddress})")
    @SelectKey(keyColumn = "id",keyProperty = "userId",before = false,resultType =Integer.class,statement = {" select last_insert_id()"})
    @ResultMap("userMap")
    void saveUser(User user);
```

**测试类：**

```java
 @Test
    public void testSave() throws Exception {
        User user = new User();
        user.setUserName("周杰伦");
        user.setUserAddress("台湾省台北市");
        user.setUserSex("男");
        user.setUserBirthday(new Date());
        userDao.saveUser(user);
        System.out.println(user);
    }
```

**打印**

![image-20230201141907451](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230201141907451.png)

***或者***

```xml
<insert id="insert" parameterType="com.xx.yy.datasource.domain.User" >
    <selectKey resultType="java.lang.Long" keyProperty="autoId" order="BEFORE" >
        SELECT LAST_INSERT_ID()
    </selectKey>
        insert into User (AutoId, UserId, Mobile, Username, CreateTime, LastModifyTime)
        values (#{autoId,jdbcType=BIGINT}, #{userId,jdbcType=BIGINT}, #{mobile,jdbcType=VARCHAR}, 
        #{username,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, #{lastModifyTime,jdbcType=TIMESTAMP})
</insert>
```

