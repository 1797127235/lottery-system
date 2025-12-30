package org.lotterysystem.dao.handle;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.lotterysystem.dao.dataobject.Encrypt;
import org.springframework.util.StringUtils;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
    数据插入数据库自动加密解密Encrypt类
    当前项目中是自动加密解密手机号
 */

@MappedTypes({Encrypt.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class EncryptTypeHandler extends BaseTypeHandler<Encrypt> {
    //密钥
    private final byte[] KEY = "1234567890123456".getBytes(StandardCharsets.UTF_8);

    /*
        设置参数
        ps SQL 预编译对象
        i 需要赋值得索引位置
        parameter原本位置i需要赋得值
        jdbcType jdbc的类型
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Encrypt parameter, JdbcType jdbcType) throws SQLException {
        if(parameter == null || parameter.getValue() == null) {
            ps.setString(i, null);
            return;
        }
        // 加密
        AES aes = SecureUtil.aes(KEY);
        String str = aes.encryptHex(parameter.getValue());
        ps.setString(i, str);
    }

    /*
        获取值
        rs 结果集合
        columnName 索引名
     */
    @Override
    public Encrypt getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return decrypt(rs.getString(columnName));
    }

    /*
        rs 结果集
        columnIndex 索引位置
     */

    @Override
    public Encrypt getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return decrypt(rs.getString(columnIndex));
    }
    /*
        cs 结果集合
        columnIndex 索引位置
     */
    @Override
    public Encrypt getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return decrypt((cs.getString(columnIndex)));
    }

    private Encrypt decrypt(String value) {
        if(!StringUtils.hasText(value)) {
            return null;
        }

        return new Encrypt(SecureUtil.aes(KEY).decryptStr(value));
    }
}
