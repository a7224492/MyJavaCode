package com.kodgames.battleserver.service.battle.region.neimeng.common.operation.filter;

import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;

/**
 * 碰牌操作过滤器
 */
public class OperationResultFilter_Peng extends OperationResultFilter
{

	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		// 跳过发牌
		if (phaseDeal)
			return true;

		// 跳过非can碰操作
		if (result.getPlayType() != PlayType.OPERATE_CAN_PENG_A_CARD)
			return true;

		// 获取操作记录
		PlayerInfo player = context.getPlayers().get(result.getRoleId());
		Macro.AssetTrue(null == player);
		List<Step> cardHeap = player.getCards().getCardHeap();
		Macro.AssetTrue(null == cardHeap);

		// 已上听不能碰牌
		if (cardHeap.stream().filter(s -> s.getPlayType() == PlayType.DISPLAY_TING).count() > 0)
			return false;

		return true;
	}

}
