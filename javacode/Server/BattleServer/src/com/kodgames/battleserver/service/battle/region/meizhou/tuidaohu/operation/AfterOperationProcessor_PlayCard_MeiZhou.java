package com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.operation.AfterOperationProcessorBase;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;

import net.sf.json.JSONObject;

/**
 * 抄庄检测
 */
public class AfterOperationProcessor_PlayCard_MeiZhou extends AfterOperationProcessorBase
{
	public static final String KEY_LIU_JU = "liuJu";
	public static final String KEY_CALCULATE_TYPE = "calculateType";
	public static final String KEY_SCORE_VALUE = "scoreValue";

	private ScoreCalculateType calculateType;
	private int scoreValue;

	/** 如果抄庄，是否流局 */
	private boolean liuJu;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);
		scoreValue = CreateContextParser.getInt(context, KEY_SCORE_VALUE);
		calculateType = CreateContextParser.getScoreCalculateType(context, KEY_CALCULATE_TYPE);
		liuJu = context.getBoolean(KEY_LIU_JU);
	}

	@Override
	public List<Step> process(ControllerManager controller, Step prevStep)
	{
		List<Step> ret = new ArrayList<>();
		BattleBean context = controller.getBattleBean();

		// 庄家上家
		PlayerInfo prePlayerInfo = context.getPlayerById(context.getPreRoleId(context.getZhuang()));

		// 抄庄只需要对庄家上家出牌后进行检测
		if (prevStep.getRoleId() != prePlayerInfo.getRoleId())
			return ret;

		// 存储玩家的打牌操作
		Map<Integer, Step> steps = new HashMap<Integer, Step>();

		// 统计玩家的打牌次数
		int count = 0;

		// 获取玩家的操作，遍历找到打牌操作
		for (Step step : context.getRecords())
		{
			// 排除吃碰杠胡操作
			if (step.getPlayType() == PlayType.OPERATE_BU_GANG_A_CARD || step.getPlayType() == PlayType.OPERATE_GANG_A_CARD || step.getPlayType() == PlayType.OPERATE_PENG_A_CARD
				|| step.getPlayType() == PlayType.OPERATE_HU)
				return ret;

			// 去掉不是打牌的操作
			if (step.getPlayType() != PlayType.OPERATE_PLAY_A_CARD)
				continue;

			// 将打牌信息存入steps中
			steps.put(step.getRoleId(), step);
			++count;
		}

		// 是否等于玩家数量
		if (count != context.getPlayerSize() || steps.size() != context.getPlayerSize())
			return ret;

		// 判断是否打的为同一张牌
		byte card = 0;
		for (Step step : steps.values())
		{
			if (card == 0)
				card = step.getCards().get(0);

			// 如果不是同一张牌，返回
			else if (card != step.getCards().get(0))
				return ret;
		}

		// 构造抄庄分数
		int zhuangId = context.getZhuang();
		PlayerInfo playerInfo = context.getPlayers().get(zhuangId);

		// 目标玩家
		Set<Integer> targets = context.getPlayerIds().stream().filter(id -> id != zhuangId).collect(Collectors.toSet());

		// 添加计分项
		ScoreData scoreData = new ScoreData();
		scoreData.setAddOperation(false);
		scoreData.setSourceId(context.getZhuang());
		targets.forEach(id -> scoreData.getScoreTargetList().add(id));

		ScorePoint scorePoint = new ScorePoint();
		scorePoint.setCalcType(calculateType.getValue());
		scorePoint.setScoreType(PlayType.DISPLAY_FOLLOW_BANKER);
		scorePoint.setScoreValue(scoreValue);
		scoreData.getPoints().add(scorePoint);

		// 添加到玩家分数中
		playerInfo.getCards().getScoreDatas().add(scoreData);

		// 流局
		if (liuJu)
		{
			context.setBattleState(BattleState.HUANGZHUANG);

			// 发送抄庄流局（播放抄庄流局动画）
			controller.addDisplayOperations(new Step(context.getZhuang(), PlayType.DISPLAY_CHAOZHUANG_LIUJU));
			controller.sendDisplayOperations();
		}
		else
		{
			// 发送抄庄（播放抄庄动画）
			controller.addDisplayOperations(new Step(context.getZhuang(), PlayType.DISPLAY_CHAOZHUANG));
			controller.sendDisplayOperations();
		}

		return ret;
	}
}
