package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.assetExistanceContext.implementations.AssetInstanceImpl;
import ar.edu.itba.paw.models.assetExistanceContext.implementations.BookImpl;
import ar.edu.itba.paw.models.assetExistanceContext.implementations.PhysicalCondition;
import ar.edu.itba.paw.models.assetExistanceContext.interfaces.Book;
import ar.edu.itba.paw.models.userContext.implementations.LocationImpl;
import ar.edu.itba.paw.models.userContext.implementations.UserImpl;
import ar.edu.itba.paw.models.userContext.interfaces.Location;
import ar.edu.itba.paw.models.userContext.interfaces.User;
import ar.itba.edu.paw.persistenceinterfaces.AssetInstanceDao;
import ar.edu.itba.paw.models.assetExistanceContext.interfaces.AssetInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AssetInstanceDaoImpl implements AssetInstanceDao {
    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<AssetInstance> ROW_MAPPER_AI = new RowMapper<AssetInstance>() {
        @Override
        public AssetInstance mapRow(ResultSet rs, int i) throws SQLException {
            String isbn = rs.getString("isbn");
            String author = rs.getString("author");
            String title = rs.getString("title");
            String language = rs.getString("language");
            Book book = new BookImpl(isbn, author, title, language);

            String zipcode = rs.getString("zipcode");
            String locality = rs.getString("locality");
            String province = rs.getString("province");
            String country = rs.getString("country");

            Location loc = new LocationImpl(zipcode, locality, province, country);

            String email = rs.getString("email");
            User user = new UserImpl(email, "", "");

            int id = rs.getInt("id");
            PhysicalCondition physicalcondition = PhysicalCondition.fromString(rs.getString("physicalcondition"));

            return new AssetInstanceImpl(id, book, physicalcondition, user, loc);
        }
    };
    private static final RowMapper<Integer> ROW_MAPPER_UID = (rs, rownum) -> rs.getInt("uid");

    @Autowired
    public AssetInstanceDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }
    @Override
    public Optional<Integer> addAssetInstance(int assetId,int ownerId,int locationId,int photoId, AssetInstance ai) {
        String query = "INSERT INTO assetinstance(assetid,owner,locationid,physicalcondition,status,photoid) VALUES(?,?,?,?,?,?)";

        jdbcTemplate.update(query,assetId,ownerId,locationId,ai.getPhysicalCondition().toString(),"",photoId);


        return Optional.empty();
    }

    @Override
    public Optional<AssetInstance> getAssetInstance(int assetId) {
        AssetInstance assetInstance;
        try {
            Object[] params = new Object[] {assetId};
            String query = "SELECT ai.id AS id, ai.photoid, ai.status, ai.physicalcondition, b.title AS title, b.isbn AS isbn, b.language AS language, b.author AS author, l.locality AS locality, l.zipcode AS zipcode, l.province AS province, l.country AS country, u.behavior AS user_behavior, u.mail AS email, u.telephone FROM assetinstance ai JOIN book b ON ai.assetid = b.uid JOIN location l ON ai.locationid = l.id LEFT JOIN users u ON ai.owner = u.id WHERE ai.id = ?";
            assetInstance = jdbcTemplate.queryForObject(query, params, ROW_MAPPER_AI);
            return Optional.of(assetInstance);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }
}
