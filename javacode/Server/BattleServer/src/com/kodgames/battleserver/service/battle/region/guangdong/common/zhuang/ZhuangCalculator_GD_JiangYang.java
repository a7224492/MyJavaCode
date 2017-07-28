package com.kodgames.battleserver.service.battle.region.guangdong.common.zhuang;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.zhuang.ZhuangCalculator;

/**
 * 广东揭阳玩法, 换庄规则
 */
public class ZhuangCalculator_GD_JiangYang extends ZhuangCalculator
{
	@Override
	public void calculateNextZhuang(BattleRoom battleRoom)
	{
		// 获得当前局
		BattleBean battleBean = battleRoom.getGames().get(battleRoom.getGames().size() - 1);

		// 黄庄, 庄家继续坐庄
		if (battleBean.getBattleState() == BattleState.HUANGZHUANG)
		{
			battleBean.setNextZhuang(battleBean.getZhuang());
			return;
		}

		// 一炮多响, 转庄到点炮的人（点炮的是庄家，不转庄）
		List<Integer> huIndices = battleBean.getHuIndex();
		if (huIndices.size() > 1)
		{
			for (Integer index : huIndices)
			{
				Step huRecord = battleBean.getRecords().get(index);
				int huSourceId = battleBean.getScoreData(huRecord.getRoleId(), index).getSourceId();
				battleBean.setNextZhuang(huSourceId);
				break;
			}

			return;
		}

		// 只有一家胡牌
		Step huRecord = battleBean.getRecords().get(battleBean.getHuIndex().get(0));
		// 判断抢杠胡, 转庄到被抢杠的人（被抢杠的是庄家，不转庄）
		{
			ScoreData scoreData = battleBean.getScoreData(huRecord.getRoleId(), battleBean.getHuIndex().get(0));
			List<ScorePoint> listScordPoint = scoreData.getPoints();
			for (ScorePoint sp : listScordPoint)
			{
				int scoreType = sp.getScoreType();
				if (scoreType == PlayType.HU_QIANG_GANG_HU)
				{
					battleBean.setNextZhuang(scoreData.getSourceId());
					return;
				}
			}
		}

		// 正常胡, 过庄到赢家
		battleBean.setNextZhuang(huRecord.getRoleId());
	}

}
