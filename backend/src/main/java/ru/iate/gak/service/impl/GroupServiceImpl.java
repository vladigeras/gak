package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iate.gak.domain.Group;
import ru.iate.gak.repository.GroupRepository;
import ru.iate.gak.service.GroupService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    @Transactional
    public List<Group> getGroups() {
        return groupRepository.getAllOrderByTitleAsc().stream().map(Group::new).collect(Collectors.toList());
    }
}
