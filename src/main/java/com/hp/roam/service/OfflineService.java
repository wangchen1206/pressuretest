package com.hp.roam.service;

import java.util.List;

import com.hp.roam.model.Offline;
import com.hp.roam.model.OfflineExample;

/**
 * @author ck
 * @date 2019年4月28日 下午2:30:49
 */
public interface OfflineService {
	long countByExample(OfflineExample example);

    int deleteByExample(OfflineExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Offline record);

    int insertSelective(Offline record);

    List<Offline> selectByExample(OfflineExample example);

    Offline selectByPrimaryKey(Integer id);

    int updateByExampleSelective(Offline record, OfflineExample example);

    int updateByExample(Offline record, OfflineExample example);

    int updateByPrimaryKeySelective(Offline record);

    int updateByPrimaryKey(Offline record);
}
