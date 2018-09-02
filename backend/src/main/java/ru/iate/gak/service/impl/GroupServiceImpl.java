package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iate.gak.model.GroupEntity;
import ru.iate.gak.repository.GroupRepository;
import ru.iate.gak.service.GroupService;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    @Transactional
    public List<GroupEntity> getGroups() {
        return groupRepository.getAllOrderByTitleAsc();
    }
}
