package miu.edu.ea.cs544.ars.service;

import miu.edu.ea.cs544.ars.domain.Session;
import miu.edu.ea.cs544.ars.repository.SessionRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SessionService {

    @Autowired
    private SessionRepositoryInterface sessionrepo;

    public List<Session> listAll() {
        return sessionrepo.findAll();
    }

    public List<Session> findByProviderId(int id) {
        return sessionrepo.findByProviderId(id);
    }

    public Session findByProviderIdAndSessionID(int id, int provider_id) {
        return sessionrepo.findByProviderIdAndSessionID(id, provider_id);
    }

    public List<Session> listByProviderId(int providerId) {
        return sessionrepo.findByProviderId(providerId);
    }

    public void save(Session session) {
        sessionrepo.save(session);
    }

    public Session get(Integer id) {
        return sessionrepo.findById(id).get();
    }

    public Session update(Session session) {
        return sessionrepo.save(session);
    }

    public void delete(int id) {
        sessionrepo.deleteById(id);
    }

}
