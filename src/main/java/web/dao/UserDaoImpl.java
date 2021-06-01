package web.dao;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import web.model.User;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager em;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public void saveUser(User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        em.persist(user);
    }

    @Override
    public void update(Long id, User user) {
//        em.merge(user);
        User userForUpdate = show(id);
        userForUpdate.setFirst_name(user.getFirst_name());
        userForUpdate.setRoles(user.getRoles());
        if(!Objects.equals(show(id).getPassword(), bCryptPasswordEncoder.encode(user.getPassword()))){
            userForUpdate.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
    }

    @Override
    public List<User> getListUsers() {
        String hql = "FROM User";
        Query query = em.createQuery(hql, User.class);
        return query.getResultList();
    }

    @Override
    public void remove(long id) {
        em.remove(show(id));
    }

    @Override
    public User show(long id) {
        return em.find(User.class, id);
    }

    @Override
    public User findUserByUsername(String username){
        String hql = "FROM User where first_name=:username";
        Query query = em.createQuery(hql, User.class)
                .setParameter("username", username);
        return (User) query.getResultList().get(0);
    }
}
