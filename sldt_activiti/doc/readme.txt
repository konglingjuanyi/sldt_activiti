注意事项说明：
   1.示例模块以角色查询为例，示例是以元数据平台命名为例，调整需根据各自平台进行命名
   2.应用的数据源访问方式为jndi方式，需在容器中进行jndi配置，配置参考如下：
          <Resource auth="Container" driverClassName="com.mysql.jdbc.Driver" 
                    maxActive="100" maxIdle="30" maxWait="10000" 
                    name="jdbc/Datasource_mp" 
                    type="javax.sql.DataSource" 
                    url="jdbc:mysql://120.27.53.93:3306/sm"
                    username="meta"
                    password="SL2015bi07@!"/>
					
   3.开发新应用，请调整里面的所有demo目录，并将示例模块删除