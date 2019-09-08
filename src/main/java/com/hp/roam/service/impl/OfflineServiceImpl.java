package com.hp.roam.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hp.roam.dao.OfflineMapper;
import com.hp.roam.model.Offline;
import com.hp.roam.model.OfflineExample;
import com.hp.roam.service.OfflineService;

/**
 * @author ck
 * @date 2019年4月28日 下午2:32:28
 */
@Service
@Transactional
public class OfflineServiceImpl implements OfflineService{

	@Autowired
	private OfflineMapper offlineMapper;
	
	/* (non-Javadoc)
	 * @see com.hp.roam.service.OfflineService#countByExample(com.hp.roam.model.OfflineExample)
	 */
	@Override
	public long countByExample(OfflineExample example) {
		return offlineMapper.countByExample(example);
	}

	/* (non-Javadoc)
	 * @see com.hp.roam.service.OfflineService#deleteByExample(com.hp.roam.model.OfflineExample)
	 */
	@Override
	public int deleteByExample(OfflineExample example) {
		return offlineMapper.deleteByExample(example);
	}

	/* (non-Javadoc)
	 * @see com.hp.roam.service.OfflineService#deleteByPrimaryKey(java.lang.Integer)
	 */
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return offlineMapper.deleteByPrimaryKey(id);
	}

	/* (non-Javadoc)
	 * @see com.hp.roam.service.OfflineService#insert(com.hp.roam.model.Offline)
	 */
	@Override
	public int insert(Offline record) {
		return offlineMapper.insert(record);
	}

	/* (non-Javadoc)
	 * @see com.hp.roam.service.OfflineService#insertSelective(com.hp.roam.model.Offline)
	 */
	@Override
	public int insertSelective(Offline record) {
		return offlineMapper.insertSelective(record);
	}

	/* (non-Javadoc)
	 * @see com.hp.roam.service.OfflineService#selectByExample(com.hp.roam.model.OfflineExample)
	 */
	@Override
	public List<Offline> selectByExample(OfflineExample example) {
		return offlineMapper.selectByExample(example);
	}

	/* (non-Javadoc)
	 * @see com.hp.roam.service.OfflineService#selectByPrimaryKey(java.lang.Integer)
	 */
	@Override
	public Offline selectByPrimaryKey(Integer id) {
		return offlineMapper.selectByPrimaryKey(id);
	}

	/* (non-Javadoc)
	 * @see com.hp.roam.service.OfflineService#updateByExampleSelective(com.hp.roam.model.Offline, com.hp.roam.model.OfflineExample)
	 */
	@Override
	public int updateByExampleSelective(Offline record,
			OfflineExample example) {
		return offlineMapper.updateByExampleSelective(record, example);
	}

	/* (non-Javadoc)
	 * @see com.hp.roam.service.OfflineService#updateByExample(com.hp.roam.model.Offline, com.hp.roam.model.OfflineExample)
	 */
	@Override
	public int updateByExample(Offline record, OfflineExample example) {
		return offlineMapper.updateByExample(record, example);
	}

	/* (non-Javadoc)
	 * @see com.hp.roam.service.OfflineService#updateByPrimaryKeySelective(com.hp.roam.model.Offline)
	 */
	@Override
	public int updateByPrimaryKeySelective(Offline record) {
		return offlineMapper.updateByPrimaryKeySelective(record);
	}

	/* (non-Javadoc)
	 * @see com.hp.roam.service.OfflineService#updateByPrimaryKey(com.hp.roam.model.Offline)
	 */
	@Override
	public int updateByPrimaryKey(Offline record) {
		return offlineMapper.updateByPrimaryKey(record);
	}

}
