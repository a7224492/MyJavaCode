package com.kodgames.battleserver.service.battle.region.guangdong.shantou.creator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.IBattleCreator;
import com.kodgames.battleserver.service.battle.core.finish.BattleFinishChecker_Common;
import com.kodgames.battleserver.service.battle.core.finish.PlayerFinishChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_13YaoHu;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_4MasterCard;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_7DuiHu;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_PingHu;
import com.kodgames.battleserver.service.battle.core.operation.AfterOperationProcessor;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker;
import com.kodgames.battleserver.service.battle.core.operation.OperationCheckerBase;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_AnGang;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_BuGang;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Gang;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Hu;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Pass;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Peng;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter_CardPoolHasEnoughtStayCard;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter_PassBuGang;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter_ScoreLimit;
import com.kodgames.battleserver.service.battle.core.playcard.PlayCardProcessor;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreCalculator;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.battle.filter.BattleScoreFilter_RemoveGangWhenHuangZhuang;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreSourceFilter_LastOperator;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreTargetFilter_Common;
import com.kodgames.battleserver.service.battle.core.score.game.GameScoreFilter;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreChecker_AnGang;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreChecker_BuGang;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreChecker_Gang;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_4MasterCard;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_PinHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_QiDui;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_ShiSanYao;
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_GangShangHua;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_HaoHuaQiDui;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_HunYaoJiu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_HunYiSe;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_PengPengHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QiangGangHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QingYiSe;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_SanHaoHuaQiDui;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_ShuangHaoHuaQiDui;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_SiGang;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_TianHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_ZiYiSe;
import com.kodgames.battleserver.service.battle.processer.BattleProcesser;
import com.kodgames.battleserver.service.battle.processer.CalculateProcesser;
import com.kodgames.battleserver.service.battle.processer.DealCardProcesser;
import com.kodgames.battleserver.service.battle.processer.InitCardPoolProcesser;
import com.kodgames.battleserver.service.battle.region.guangdong.chaoshan.processor.Processor_XiaoHu;
import com.kodgames.battleserver.service.battle.region.guangdong.common.Rules_GuangDong;
import com.kodgames.battleserver.service.battle.region.guangdong.common.operation.AfterOperationProcessor_PlayCard_ChaoShan;
import com.kodgames.battleserver.service.battle.region.guangdong.common.operation.filter.OperationResultFilter_PassHu_ChaoShan;
import com.kodgames.battleserver.service.battle.region.guangdong.common.operation.filter.OperationResultFilter_PingHuMastZiMo;
import com.kodgames.battleserver.service.battle.region.guangdong.common.processor.DisplayStepProcessor_HideBuyHorseCard;
import com.kodgames.battleserver.service.battle.region.guangdong.common.processor.Processer_LianZhuang;
import com.kodgames.battleserver.service.battle.region.guangdong.common.processor.Processor_BettingHorse;
import com.kodgames.battleserver.service.battle.region.guangdong.common.processor.Processor_CalculateBuyHorse;
import com.kodgames.battleserver.service.battle.region.guangdong.common.processor.Processor_CheckMasterCard;
import com.kodgames.battleserver.service.battle.region.guangdong.common.processor.Processor_StartBuyHorse;
import com.kodgames.battleserver.service.battle.region.guangdong.common.processor.ZhuangInfoProcesser_GuangDong;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.battle.BattleScoreCalculator_GuangDong;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.filter.ScoreTargetFilter_ZiMo_TingScoreLimit;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.game.GameScoreCalculator_GD;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.game.GameScoreFilter_GD;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.hu.HuSubScoreChecker_ChiGangGangBao;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.hu.HuSubScoreChecker_NoMaster;
import com.kodgames.battleserver.service.battle.region.guangdong.common.zhuang.ZhuangCalculator_GD_JiangYang;
import com.kodgames.battleserver.service.battle.region.guangdong.shantou.score.filter.ScoreTargetFilter_GangBaoQuanBao_ShanTou;
import com.kodgames.battleserver.service.battle.region.guangdong.shantou.score.hu.HuSubScoreChecker_HaiLao_ShanTou;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_DaSanYuan;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_DaSiXi;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_DiHu_ShanWei;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_XiaoSanYuan;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_XiaoSiXi;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_YiTiaoLong_ShanWei;

/**
 * 汕头规则的creator
 */
public class BattleCreator_GuangDong_ShanTou implements IBattleCreator
{
	/**
	 * 牌型和规则的对应数组，用LinkedHashMap是为了让KeySet为有序的
	 */
	private static Map<Integer, Integer> cardType_RulesMap = new LinkedHashMap<Integer, Integer>();

	static
	{
		// 牌型和规则的对应
		{
			// 碰碰胡
			cardType_RulesMap.put(PlayType.HU_PENG_PENG_HU, Rules_GuangDong.SHAN_TOU_PENG_PENG_HU);
			// 混一色
			cardType_RulesMap.put(PlayType.HU_HUN_YI_SE, Rules_GuangDong.SHAN_TOU_HUN_YI_SE);
			// 清一色
			cardType_RulesMap.put(PlayType.HU_QING_YI_SE, Rules_GuangDong.SHAN_TOU_QING_YI_SE);
			// 一条龙
			cardType_RulesMap.put(PlayType.HU_YI_TIAO_LONG, Rules_GuangDong.SHAN_TOU_YI_TIAO_LONG);
			// 七对
			cardType_RulesMap.put(PlayType.HU_QI_DUI, Rules_GuangDong.SHAN_TOU_QI_DUI_ALL);
			// 幺九
			cardType_RulesMap.put(PlayType.HU_HUN_YAO_JIU, Rules_GuangDong.SHAN_TOU_YAO_JIU);
			// 小三元
			cardType_RulesMap.put(PlayType.HU_XIAO_SAN_YUAN, Rules_GuangDong.SHAN_TOU_XIAO_SAN_YUAN);
			// 小四喜
			cardType_RulesMap.put(PlayType.HU_XIAO_SI_XI, Rules_GuangDong.SHAN_TOU_XIAO_SI_XI);
			// 豪华七对
			cardType_RulesMap.put(PlayType.HU_HAO_HUA_QI_DUI, Rules_GuangDong.SHAN_TOU_QI_DUI_ALL);
			// 字一色
			cardType_RulesMap.put(PlayType.HU_ZI_YI_SE, Rules_GuangDong.SHAN_TOU_ZI_YI_SE);
			// 双豪华七对
			cardType_RulesMap.put(PlayType.HU_SHUANG_HAO_HUA_QI_DUI, Rules_GuangDong.SHAN_TOU_QI_DUI_ALL);
			// 大三元
			cardType_RulesMap.put(PlayType.HU_DA_SAN_YUAN, Rules_GuangDong.SHAN_TOU_DA_SAN_YUAN);
			// 大四喜
			cardType_RulesMap.put(PlayType.HU_DA_SI_XI, Rules_GuangDong.SHAN_TOU_DA_SI_XI);
			// 三豪华七对
			cardType_RulesMap.put(PlayType.HU_SAN_HAO_HUA_QI_DUI, Rules_GuangDong.SHAN_TOU_QI_DUI_ALL);
			// 十八罗汉
			cardType_RulesMap.put(PlayType.HU_SI_GANG, Rules_GuangDong.SHAN_TOU_SHI_BA_LUO_HAN);
			// 地胡
			cardType_RulesMap.put(PlayType.HU_DI_HU, Rules_GuangDong.SHAN_TOU_TIAN_DI);
			// 天湖
			cardType_RulesMap.put(PlayType.HU_TIAN_HU, Rules_GuangDong.SHAN_TOU_TIAN_DI);
		}
	}

	public JSONObject create(List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleHelper.class.getSimpleName());
		createZhuangCalculator(result, rules);
		createHuCalculateProcessor(result, rules);
		createHuCheckerProcessor(result, rules);
		createBattleScoreCalculator(result, rules);
		createBattleFinishChecker(result, rules);
		createMainProcessor(result, rules);
		createControllerManager(result, rules);
		return result;
	}

	@Override
	public boolean checkRules(List<Integer> rules)
	{
		return Rules_GuangDong.checkRules(rules, Rules_GuangDong.GAME_TYPE_SHAN_TOU);
	}

	private void createZhuangCalculator(JSONObject context, List<Integer> rules)
	{
		// 创建庄家计算器
		context.element(BattleHelper.KEY_ZHUANG_CALCULATOR, CreateContextHelper.createObject(ZhuangCalculator_GD_JiangYang.class));
	}

	/**
	 * 创建controllerManager
	 */
	private void createControllerManager(JSONObject context, List<Integer> rules)
	{
		JSONObject obj = CreateContextHelper.createObject(ControllerManager.class);
		{
			JSONArray arrayContext = CreateContextHelper.createArray();
			// 在这里配置需要的处理
			{
				JSONObject hideObj = CreateContextHelper.createObject(DisplayStepProcessor_HideBuyHorseCard.class);
				hideObj.element(ControllerManager.KEY_DISPLAY_STEP_PLAY_TYPE, PlayType.OPERATE_DEAL_FIRST);

				{
					// 配置需要隐藏的type
					JSONArray hideArray = CreateContextHelper.createArray();
					hideArray.element(PlayType.DISPLAY_BUY_HORSE);
					hideArray.element(PlayType.DISPLAY_PUNISH_HORSE);
					hideObj.element(DisplayStepProcessor_HideBuyHorseCard.KEY_HIDE_TYPES, hideArray);
				}

				arrayContext.element(hideObj);
			}

			obj.element(ControllerManager.KEY_DISPLAY_STEP_PROCESSORS, arrayContext);
		}

		context.element(BattleHelper.KEY_CONTROLLER_MANAGER, obj);
	}

	private void createMainProcessor(JSONObject context, List<Integer> rules)
	{
		JSONArray result = CreateContextHelper.createArray();

		// 确定庄家
		result.element(CreateContextHelper.createObject(ZhuangInfoProcesser_GuangDong.class).element(ZhuangInfoProcesser_GuangDong.KEY_NEED_LIANZHUANG,
			rules.contains(Rules_GuangDong.SCORE_LIAN_ZHUANG)));

		// 初始化牌池
		{
			JSONObject subContext = CreateContextHelper.createObject(InitCardPoolProcesser.class);

			// 设置支持什么类型的牌
			{
				JSONArray supportTypeContext = CreateContextHelper.createArray();

				// 无字牌/有字牌
				if (rules.contains(Rules_GuangDong.GAME_PLAY_NO_WAN) == false)
					supportTypeContext.add(MahjongConstant.CardType.WAN.toString());
				supportTypeContext.add(MahjongConstant.CardType.TIAO.toString());
				supportTypeContext.add(MahjongConstant.CardType.TONG.toString());

				// 带风/不带风
				if (rules.contains(Rules_GuangDong.GAME_PLAY_NO_ZI) == false)
					supportTypeContext.add(MahjongConstant.CardType.ZI.toString());

				subContext.element(InitCardPoolProcesser.KEY_SUPPORT_CARD_TYPES, supportTypeContext);
			}

			result.element(subContext);
		}

		// 发牌
		result.element(CreateContextHelper.createObject(DealCardProcesser.class));

		// 鬼牌
		if (rules.contains(Rules_GuangDong.MASTER_CARD_HONG_ZHONG) || rules.contains(Rules_GuangDong.MASTER_CARD_BAI_BAN) || rules.contains(Rules_GuangDong.MASTER_CARD_GENERATE))
		{
			JSONObject subContext = CreateContextHelper.createObject(Processor_CheckMasterCard.class);

			// 无鬼牌/红中做鬼/白板做鬼/翻鬼（双鬼）
			if (rules.contains(Rules_GuangDong.MASTER_CARD_HONG_ZHONG))
				subContext.element(Processor_CheckMasterCard.KEY_DEFAULT_MASTER_CARD, MahjongConstant.CardType.ZI.Value() + 4);
			else if (rules.contains(Rules_GuangDong.MASTER_CARD_BAI_BAN))
				subContext.element(Processor_CheckMasterCard.KEY_DEFAULT_MASTER_CARD, MahjongConstant.CardType.ZI.Value() + 6);
			else if (rules.contains(Rules_GuangDong.MASTER_CARD_TOW_MASTER))
				subContext.element(Processor_CheckMasterCard.KEY_GENERATE_MASTER_CARD_COUNT, 2);
			else
				subContext.element(Processor_CheckMasterCard.KEY_GENERATE_MASTER_CARD_COUNT, 1);

			result.element(subContext);
		}

		// 买马罚马
		if (rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.BUY_HORSE_2) || rules.contains(Rules_GuangDong.PUNISH_HORSE_1)
			|| rules.contains(Rules_GuangDong.PUNISH_HORSE_2))
		{
			JSONObject object = CreateContextHelper.createObject(Processor_StartBuyHorse.class);
			// 配置翻的马牌数量
			if (rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.PUNISH_HORSE_1))
				object.element(Processor_StartBuyHorse.KEY_HORSE_NUM, 1);
			else
				object.element(Processor_StartBuyHorse.KEY_HORSE_NUM, 2);
			// 发送给客户端的类型(买马或者罚马)
			object.element(Processor_StartBuyHorse.KEY_HORSE_TYPE, (rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.BUY_HORSE_2) ? PlayType.DISPLAY_BUY_HORSE
				: PlayType.DISPLAY_PUNISH_HORSE));

			// 是否为买马
			object.element(Processor_StartBuyHorse.KEY_IS_BUY, rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.BUY_HORSE_2));
			result.element(object);
		}

		// 打牌
		result.element(createBattleProcessor(rules));

		// 配置全牌型分数计算器（必须在奖马前，删除分数的时候没关注奖马分）
		if (rules.contains(Rules_GuangDong.SHAN_TOU_QUAN_PAI_XING))
		{
			JSONObject xiaoHuContext = CreateContextHelper.createObject(Processor_XiaoHu.class);

			// 配置小胡规则下大胡的分值
			xiaoHuContext.element(Processor_XiaoHu.KEY_HU_SCORE_VALUE, 2);
			xiaoHuContext.element(Processor_XiaoHu.KEY_HU_SCORE_TYPE, PlayType.HU_QUAN_PAI_XING);
			xiaoHuContext.element(Processor_XiaoHu.KEY_HU_CALC_TYPE, ScoreCalculateType.TOTAL_ADD.getValue());

			result.element(xiaoHuContext);
		}

		// 连庄
		if (rules.contains(Rules_GuangDong.SCORE_LIAN_ZHUANG))
		{
			JSONObject lianZhuangContext = CreateContextHelper.createObject(Processer_LianZhuang.class);
			{
				JSONArray arrayContext = CreateContextHelper.createArray();

				arrayContext.element(PlayType.HU_QIANG_GANG_HU);
				arrayContext.element(PlayType.HU_GANG_SHANG_HUA);
				arrayContext.element(PlayType.HU_HAI_DI_LAO_YUE);
				arrayContext.element(PlayType.DISPLAY_NO_MASTER_CARD);
				arrayContext.element(PlayType.HU_CHI_GANG_GANG_BAO);

				lianZhuangContext.element(Processer_LianZhuang.KEY_FINAL_CALC_TYPES, arrayContext);
			}
			result.element(lianZhuangContext);
		}

		// 买马罚马结算
		if (rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.BUY_HORSE_2) || rules.contains(Rules_GuangDong.PUNISH_HORSE_1)
			|| rules.contains(Rules_GuangDong.PUNISH_HORSE_2))
		{
			JSONObject object = CreateContextHelper.createObject(Processor_CalculateBuyHorse.class);

			// 是否为买马
			object.element(Processor_CalculateBuyHorse.KEY_IS_BUY, rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.BUY_HORSE_2));
			// 三人玩法
			object.element(Processor_CalculateBuyHorse.KEY_IS_THREE_PLAYER, rules.contains(Rules_GuangDong.GAME_PLAY_THREE_PLAYER));

			if (rules.contains(Rules_GuangDong.HU_ZI_MO_TARGET_SCORE_LIMIT))
			{
				// 十倍不计分
				object.element(Processor_CalculateBuyHorse.KEY_SCORE_LIMIT_CALCTYPE, ScoreCalculateType.TOTAL_ADD);
				object.element(Processor_CalculateBuyHorse.KEY_SCORE_LIMIT_VALUE, 20);
			}

			// 需要删除的分数
			{
				JSONArray ingoreTypes = CreateContextHelper.createArray();

				ingoreTypes.element(PlayType.HU_QIANG_GANG_HU);
				ingoreTypes.element(PlayType.HU_CHI_GANG_GANG_BAO);

				object.element(Processor_CalculateBuyHorse.KEY_INGORE_TYPE, ingoreTypes);
			}
			result.element(object);
		}

		// 奖马
		if (rules.contains(Rules_GuangDong.BETTING_HOUSE_2) || rules.contains(Rules_GuangDong.BETTING_HOUSE_5) || rules.contains(Rules_GuangDong.BETTING_HOUSE_8))
		{
			JSONObject subContext = CreateContextHelper.createObject(Processor_BettingHorse.class);

			// 三人玩法
			subContext.element(Processor_BettingHorse.KEY_IS_THREE_PLAYER, rules.contains(Rules_GuangDong.GAME_PLAY_THREE_PLAYER));

			// 马跟杠
			subContext.element(Processor_BettingHorse.KEY_APPLY_TO_GANG, rules.contains(Rules_GuangDong.BETTING_HOUSE_WITH_GANG));

			result.element(subContext);
		}

		// 分数计算
		result.element(createCalculate(rules));

		context.element(MahjongConstant.JSON_PROC, result);
	}

	private JSONObject createCalculate(List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject(CalculateProcesser.class);

		// 牌局相关分数
		{
			JSONObject context = CreateContextHelper.createObject();

			// 牌局分数添加
			{
				JSONArray arrayContext = CreateContextHelper.createArray();

				context.element(BattleScoreProcessor.KEY_POINT_CALCULATORS, arrayContext);
			}

			// 牌局分数过滤
			{
				JSONArray arrayContext = CreateContextHelper.createArray();

				// (规则不包含流局算杠就移除掉杠分）
				if (rules.contains(Rules_GuangDong.LIU_JU_SUAN_GANG) == false)
					arrayContext.add(CreateContextHelper.createObject(BattleScoreFilter_RemoveGangWhenHuangZhuang.class));

				context.element(BattleScoreProcessor.KEY_SCORE_FILTERS, arrayContext);
			}

			createContext.element(CalculateProcesser.KEY_BATTLE_SCORE_PROCESSOR, context);
		}

		// 房间分数计算器
		{
			JSONObject context = CreateContextHelper.createObject(GameScoreCalculator_GD.class);
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 设置房间分数统计
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.HU_ZI_MO, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.HU_DIAN_PAO, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.HU_DIAN_PAO, false, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_AN_GANG, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_GANG_A_CARD, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_BU_GANG_A_CARD, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_ALL_HORSE_CARD_COUNT, true, true));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_LIAN_ZHUANG, true, true));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_FOLLOW_BANKER, true, false));

			{
				JSONArray countArray = CreateContextHelper.createArray();
				// 买马罚马
				if (rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.BUY_HORSE_2) || rules.contains(Rules_GuangDong.PUNISH_HORSE_1)
					|| rules.contains(Rules_GuangDong.PUNISH_HORSE_2) || rules.contains(Rules_GuangDong.BETTING_HOUSE_2) || rules.contains(Rules_GuangDong.BETTING_HOUSE_5)
					|| rules.contains(Rules_GuangDong.BETTING_HOUSE_8))
				{
					countArray.element(createGameScoreFilter(PlayType.DISPLAY_ALL_HORSE_CARD_COUNT, true, false, true, new int[] {PlayType.DISPLAY_LOSE_HORSE_CARD, PlayType.DISPLAY_WIN_HORSE_CARD,
						PlayType.DISPLAY_BETTING_HOUSE}));
				}
				// 抢杠胡配置到点炮类中
				{
					countArray.element(createGameScoreFilter(PlayType.HU_DIAN_PAO, true, false, false, new int[] {PlayType.HU_QIANG_GANG_HU}));
				}
				// 抢杠胡配置到被动点炮类中
				{
					countArray.element(createGameScoreFilter(PlayType.HU_DIAN_PAO, false, false, false, new int[] {PlayType.HU_QIANG_GANG_HU}));
				}
				// 杠爆，海捞配置到自摸中
				{
					countArray.element(createGameScoreFilter(PlayType.HU_ZI_MO, true, false, false, new int[] {PlayType.HU_GANG_SHANG_HUA, PlayType.HU_HAI_DI_LAO_YUE}));
				}

				context.element(GameScoreCalculator_GD.KEY_NEED_ADD_TRPE_MAP, countArray);
			}

			context.element(GameScoreCalculator_GD.KEY_GAME_SCORE_FILTERS_GD, arrayContext);
			createContext.element(CalculateProcesser.KEY_GAME_SCORE_CALCULATOR, context);
		}

		return createContext;
	}

	/**
	 * 创建潮汕地区的gameScoreFilter
	 */
	private JSONObject createGameScoreFilter(int addToType, boolean addOperation, boolean calculateScorePoint, boolean checkStep, int[] needAddTypes)
	{
		JSONObject gameScoreFilter = CreateContextHelper.createObject();
		JSONArray needAddTypeArray = CreateContextHelper.createArray();

		for (int index = 0; index < needAddTypes.length; ++index)
		{
			needAddTypeArray.element(needAddTypes[index]);
		}

		gameScoreFilter.element(GameScoreFilter_GD.KEY_NEED_ADD_TYPES, needAddTypeArray);
		gameScoreFilter.element(GameScoreFilter_GD.KEY_ADD_TO_TYPE, addToType);
		gameScoreFilter.element(GameScoreFilter_GD.KEY_ADD_TO_TYPE_OPER, addOperation);
		gameScoreFilter.element(GameScoreFilter_GD.KEY_CALCULATE_SCORE_POINT, calculateScorePoint);
		gameScoreFilter.element(GameScoreFilter_GD.KEY_CHECK_STEP, checkStep);
		return gameScoreFilter;
	}

	/**
	 * 创建战斗相关处理器，比如前置操作处理和后置操作处理
	 * 
	 * @param rules
	 * @return
	 */
	private JSONObject createBattleProcessor(List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleProcesser.class);

		createOperationChecker(result, rules);
		createAfterOperationProcessor(result, rules);
		createPlayerCardProcessor(result, rules);
		createPlayerFinishFilter(result, rules);
		createGangScoreProcessor(result, rules);

		return result;
	}

	private void createPlayerCardProcessor(JSONObject context, List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject();
		context.element(PlayCardProcessor.class.getSimpleName(), result);
	}

	private void createOperationChecker(JSONObject context, List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject();

		{
			JSONArray proc = CreateContextHelper.createArray();

			// 碰检测
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_Peng.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 明杠检测
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_Gang.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 抓到牌库最后一张牌时，不让明杠 暗杠、补杠
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_CardPoolHasEnoughtStayCard.class));

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 暗杠
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_AnGang.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 抓到牌库最后一张牌时，不让明杠 暗杠、补杠
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_CardPoolHasEnoughtStayCard.class));

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 补杠
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_BuGang.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 抓到牌库最后一张牌时，不让明杠 暗杠、补杠
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_CardPoolHasEnoughtStayCard.class));

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 明杠必须是在抓到的这张牌触发，如果这次没有杠，以后不能杠了
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_PassBuGang.class));

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 胡牌
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_Hu.class);

				{
					JSONArray preFilters = CreateContextHelper.createArray();

					// 前置操作必须是补杠，出牌，摸牌
					preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_BU_GANG_A_CARD));
					preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_DEAL));
					// 如果不包含自摸，或者包含20分可吃胡选项就添加打牌到胡牌的前置检查中
					if (rules.contains(Rules_GuangDong.HU_ZI_MO_SHAN_TOU) == false || rules.contains(Rules_GuangDong.HU_CHI_HU_ZI_MO_LIMIT))
						preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_PLAY_A_CARD));

					processor.element(OperationCheckerBase.KEY_PRE_FILTER, preFilters);
				}

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 鸡胡不能吃胡 在大胡规则中，如果牌型为鸡胡则只能自摸
					if (rules.contains(Rules_GuangDong.SCORE_XIAO_HU_MUST_ZI_MO))
						afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_PingHuMastZiMo.class));

					// 只能自摸
					{
						// 选择了自摸并且选了20分可以吃胡
						if (rules.contains(Rules_GuangDong.HU_ZI_MO_SHAN_TOU) && rules.contains(Rules_GuangDong.HU_CHI_HU_ZI_MO_LIMIT))
						{
							JSONObject filter = CreateContextHelper.createObject(OperationResultFilter_ScoreLimit.class);
							filter.element(OperationResultFilter_ScoreLimit.KEY_CALC_TYPE, ScoreCalculateType.TOTAL_ADD.toString());

							filter.element(OperationResultFilter_ScoreLimit.KEY_VALUE, 20);

							afterFilters.element(filter);
						}
					}

					// 漏胡, 自摸不算漏胡
					{
						JSONObject filter = CreateContextHelper.createObject(OperationResultFilter_PassHu_ChaoShan.class);
						filter.element(OperationResultFilter_PassHu_ChaoShan.KEY_CHECK_PASS_ZI_MO_HU, true);
						filter.element(OperationResultFilter_PassHu_ChaoShan.KEY_CHECK_PASS_DIAN_HU, true);
						afterFilters.element(filter);
					}

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);

				}

				proc.element(processor);
			}

			// pass检测
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_Pass.class);
				proc.element(processor);
			}

			result.element(OperationChecker.KEY_OPERATION_CHECKERS, proc);
		}

		context.element(OperationChecker.class.getSimpleName(), result);
	}

	/**
	 * 配置附加操作处理器
	 */
	private void createAfterOperationProcessor(JSONObject context, List<Integer> rules)
	{
		// 一个玩家只能胡一次
		JSONObject result = CreateContextHelper.createObject();

		// 添加跟庄
		{
			if (rules.contains(Rules_GuangDong.SCORE_GEN_ZHUANG))
			{
				JSONArray afterFilters = CreateContextHelper.createArray();
				{
					JSONObject afterProcess = CreateContextHelper.createObject(AfterOperationProcessor_PlayCard_ChaoShan.class);

					afterProcess.element(AfterOperationProcessor_PlayCard_ChaoShan.KEY_CALCULATE_TYPE, ScoreCalculateType.TOTAL_ADD);
					afterProcess.element(AfterOperationProcessor_PlayCard_ChaoShan.KEY_SCORE_VALUE, -1);
					afterProcess.element(AfterOperationProcessor_PlayCard_ChaoShan.KEY_PLAY_TYPE, PlayType.OPERATE_PLAY_A_CARD);
					afterProcess.element(AfterOperationProcessor_PlayCard_ChaoShan.KEY_LIU_JU, false);

					afterFilters.add(afterProcess);
				}

				result.element(AfterOperationProcessor.KEY_PROCESSORS, afterFilters);
			}
		}

		context.element(AfterOperationProcessor.class.getSimpleName(), result);
	}

	/**
	 * 配置玩家结束条件
	 */
	private void createPlayerFinishFilter(JSONObject context, List<Integer> rules)
	{
		// 一个玩家只能胡一次
		JSONObject result = CreateContextHelper.createObject();
		result.element(PlayerFinishChecker.KEY_LIMIT_MULTI_HU, true);
		context.element(PlayerFinishChecker.class.getSimpleName(), result);
	}

	/**
	 * 创建战斗分数计算器
	 */
	private void createBattleScoreCalculator(JSONObject createContext, List<Integer> rules)
	{
		JSONObject context = CreateContextHelper.createObject(BattleScoreCalculator_GuangDong.class);

		// 设置封顶
		if (rules.contains(Rules_GuangDong.SCORE_LIMIT_5))
			context.element(BattleScoreCalculator.KEY_MAX_VALUE, 10);
		else if (rules.contains(Rules_GuangDong.SCORE_LIMIT_10))
			context.element(BattleScoreCalculator.KEY_MAX_VALUE, 20);
		else
			context.element(BattleScoreCalculator.KEY_MAX_VALUE, 0);

		{
			JSONArray array = CreateContextHelper.createArray();

			array.element(PlayType.HU_HAI_DI_LAO_YUE);
			array.element(PlayType.HU_QIANG_GANG_HU);
			array.element(PlayType.HU_GANG_SHANG_HUA);

			context.element(BattleScoreCalculator_GuangDong.KEY_HUTYPSE, array);
		}

		// 买马罚马不需要删除的类型
		if (rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.BUY_HORSE_2) || rules.contains(Rules_GuangDong.PUNISH_HORSE_1)
			|| rules.contains(Rules_GuangDong.PUNISH_HORSE_2))
		{
			// 是否为买马
			boolean isBuy = rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.BUY_HORSE_2);
			// 添加需要忽略的类型
			{
				JSONArray arrayContext = CreateContextHelper.createArray();
				arrayContext.add(isBuy ? PlayType.DISPLAY_BUY_HORSE : PlayType.DISPLAY_PUNISH_HORSE);
				arrayContext.add(isBuy ? PlayType.DISPLAY_HU_BUY_HORSE_SCORE : PlayType.DISPLAY_HU_PUNISH_HORSE_SCORE);
				arrayContext.add(isBuy ? PlayType.DISPLAY_BE_HU_BUY_HORSE_SCORE : PlayType.DISPLAY_BE_HU_PUNISH_HORSE_SCORE);
				context.element(BattleScoreCalculator_GuangDong.KEY_INGORE_TYPES, arrayContext);
			}
			// 添加马分的类型
			{
				JSONArray arrayContext = CreateContextHelper.createArray();
				arrayContext.add(isBuy ? PlayType.DISPLAY_BE_HU_BUY_HORSE_SCORE : PlayType.DISPLAY_BE_HU_PUNISH_HORSE_SCORE);
				arrayContext.add(isBuy ? PlayType.DISPLAY_HU_BUY_HORSE_SCORE : PlayType.DISPLAY_HU_PUNISH_HORSE_SCORE);
				context.element(BattleScoreCalculator_GuangDong.KEY_BUY_MA_TYPES, arrayContext);
			}
		}

		// 连庄
		if (rules.contains(Rules_GuangDong.SCORE_LIAN_ZHUANG))
		{
			{
				JSONArray arrayContext = CreateContextHelper.createArray();
				arrayContext.add(PlayType.DISPLAY_LIAN_ZHUANG);
				context.element(BattleScoreCalculator_GuangDong.KEY_OTHER_ADD_TYPE, arrayContext);
			}

			{
				JSONArray arrayContext = CreateContextHelper.createArray();
				arrayContext.add(PlayType.HU_GANG_SHANG_HUA);
				arrayContext.add(PlayType.HU_HAI_DI_LAO_YUE);
				arrayContext.add(PlayType.DISPLAY_NO_MASTER_CARD);
				context.element(BattleScoreCalculator_GuangDong.KEY_OTHER_MULTI_TYPE, arrayContext);
			}
		}

		createContext.element(BattleHelper.KEY_BATTLE_SCORE_CALCULATOR, context);
	}

	/**
	 * 设置牌局结束条件
	 */
	private void createBattleFinishChecker(JSONObject context, List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleFinishChecker_Common.class);

		// 如果选择了奖马，要留够奖马的牌，提前流局
		if (rules.contains(Rules_GuangDong.BETTING_HOUSE_2))
			result.element(BattleFinishChecker_Common.KEY_STAY_CARD_COUNT, 2);
		else if (rules.contains(Rules_GuangDong.BETTING_HOUSE_5))
			result.element(BattleFinishChecker_Common.KEY_STAY_CARD_COUNT, 5);
		else if (rules.contains(Rules_GuangDong.BETTING_HOUSE_8))
			result.element(BattleFinishChecker_Common.KEY_STAY_CARD_COUNT, 8);
		else
			result.element(BattleFinishChecker_Common.KEY_STAY_CARD_COUNT, 0);

		// 只要胡一次就可以结束牌局
		result.element(BattleFinishChecker_Common.KEY_HU_COUNT, 1);

		context.element(BattleHelper.KEY_BATTLE_FINISH_CHECKER, result);
	}

	/**
	 * 创建杠分数计算器
	 */
	private void createGangScoreProcessor(JSONObject context, List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject();

		{
			// 分数来源过滤器
			JSONArray arrayContext = CreateContextHelper.createArray();
			arrayContext.add(CreateContextHelper.createObject(ScoreSourceFilter_LastOperator.class));
			createContext.element(GangScoreProcessor.KEY_SOURCE_PLAYER_FILTERS, arrayContext);
		}

		{
			// 分数收分目标过滤器
			JSONArray arrayContext = CreateContextHelper.createArray();
			arrayContext.add(CreateContextHelper.createObject(ScoreTargetFilter_Common.class));
			createContext.element(GangScoreProcessor.KEY_SCORE_TARGET_FILTERS, arrayContext);
		}

		{
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 明杠分数计算
			arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_Gang.class, ScoreCalculateType.TOTAL_ADD, 3));

			// 暗杠分数计算
			arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_AnGang.class, ScoreCalculateType.TOTAL_ADD, 2));

			// 补杠分数计算
			arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_BuGang.class, ScoreCalculateType.TOTAL_ADD, 1));

			createContext.element(GangScoreProcessor.KEY_POINT_CACULATORS, arrayContext);
		}

		context.element(GangScoreProcessor.class.getSimpleName(), createContext);
	}

	/**
	 * 创建胡Checker
	 */
	private void createHuCheckerProcessor(JSONObject context, List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject(HuChecker.class);

		/**
		 * 胡牌牌形检查器
		 */
		{
			JSONArray checkers = CreateContextHelper.createArray();

			// 四鬼胡牌
			if (rules.contains(Rules_GuangDong.MASTER_CARD_FORE_HU))
				checkers.element(CreateContextHelper.createObject(HuChecker_4MasterCard.class));
			checkers.element(CreateContextHelper.createObject(HuChecker_PingHu.class));
			checkers.element(CreateContextHelper.createObject(HuChecker_7DuiHu.class));
			checkers.element(CreateContextHelper.createObject(HuChecker_13YaoHu.class));
			createContext.element(HuChecker.KEY_CHECKERS, checkers);
		}

		context.element(BattleHelper.KEY_HU_CHECK_PROCESSOR, createContext);
	}

	/**
	 * 胡牌得分计算器
	 */
	private void createHuCalculateProcessor(JSONObject context, List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject(HuScoreProcessor.class);
		{
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 普通规则
			arrayContext.add(CreateContextHelper.createObject(ScoreSourceFilter_LastOperator.class));

			createContext.element(HuScoreProcessor.KEY_SOURCE_PLAYER_FILTERS, arrayContext);
		}

		{
			JSONArray arrayContext = CreateContextHelper.createArray();

			arrayContext.add(CreateContextHelper.createObject(ScoreTargetFilter_Common.class));

			// 3. 10倍不计分
			if (rules.contains(Rules_GuangDong.HU_ZI_MO_TARGET_SCORE_LIMIT))
			{
				JSONObject limitContext = CreateContextHelper.createObject(ScoreTargetFilter_ZiMo_TingScoreLimit.class);
				limitContext.element(ScoreTargetFilter_ZiMo_TingScoreLimit.KEY_SCORE_LIMIT, 20);
				arrayContext.add(limitContext);
			}

			// 吃杠杠爆全包
			if (rules.contains(Rules_GuangDong.CHI_GANG_GANG_BAO_QUAN_BAO))
				arrayContext.add(CreateContextHelper.createObject(ScoreTargetFilter_GangBaoQuanBao_ShanTou.class));

			createContext.element(HuScoreProcessor.KEY_SCORE_TARGET_FILTERS, arrayContext);
		}

		// 支持的分数
		{
			boolean smallHu = rules.contains(Rules_GuangDong.SHAN_TOU_XIAO_HU);

			JSONArray arrayContext = CreateContextHelper.createArray();

			// 2. 大对子/碰碰胡：成牌时有4个三张相同的和1个对 +4（未选情况下小胡4分，大胡两分）
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_PengPengHu.class, ScoreCalculateType.TOTAL_ADD, rules.contains(Rules_GuangDong.SHAN_TOU_PENG_PENG_HU) ? 4
				: smallHu ? 4 : 2, getMutexScoreTypes(PlayType.HU_PENG_PENG_HU, rules)));

			// 1、 混一色：字牌及另外的单一花色（筒 条 万）组成 +4
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HunYiSe.class, ScoreCalculateType.TOTAL_ADD, rules.contains(Rules_GuangDong.SHAN_TOU_HUN_YI_SE) ? 4
				: smallHu ? 4 : 2, getMutexScoreTypes(PlayType.HU_HUN_YI_SE, rules)));

			// 2、 青一色：由全部14张牌为同一种牌型（筒子、万子、条子） +6
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingYiSe.class, ScoreCalculateType.TOTAL_ADD, rules.contains(Rules_GuangDong.SHAN_TOU_QING_YI_SE) ? 4
				: smallHu ? 4 : 2, getMutexScoreTypes(PlayType.HU_QING_YI_SE, rules)));

			// 3、 字一色：全部由字牌组成 +20
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ZiYiSe.class, ScoreCalculateType.TOTAL_ADD, rules.contains(Rules_GuangDong.SHAN_TOU_ZI_YI_SE) ? 20
				: smallHu ? 4 : 2, getMutexScoreTypes(PlayType.HU_ZI_YI_SE, rules)));

			// 1. 平胡（鸡胡）：四坎牌+一对将 +2
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_PinHu.class, ScoreCalculateType.TOTAL_ADD, 2));

			// 2、 十三幺：3种序数牌的一、九牌，7种字牌共13张中的12个单张及另外一对作将组成的胡牌 +26（未选情况下小胡4分。大胡两分）
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_ShiSanYao.class, ScoreCalculateType.TOTAL_ADD, rules.contains(Rules_GuangDong.SHAN_TOU_SHI_SAN_YAO) ? 26
				: smallHu ? 4 : 2));

			// 3. 七小对：由7个对子组成胡牌 +6（未选情况下：小胡4分，大胡2分）
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_QiDui.class, ScoreCalculateType.TOTAL_ADD, rules.contains(Rules_GuangDong.SHAN_TOU_QI_DUI_ALL) ? 6
				: smallHu ? 4 : 2, getMutexScoreTypes(PlayType.HU_QI_DUI, rules)));

			// 4. 豪华七小对：7小对中，有1个4张牌一样 +10
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HaoHuaQiDui.class, ScoreCalculateType.TOTAL_ADD, rules.contains(Rules_GuangDong.SHAN_TOU_QI_DUI_ALL) ? 10
				: smallHu ? 4 : 2, getMutexScoreTypes(PlayType.HU_HAO_HUA_QI_DUI, rules)));

			// 5. 双豪华七小对：7小对中，有2个4张牌一样 +20
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ShuangHaoHuaQiDui.class, ScoreCalculateType.TOTAL_ADD, rules.contains(Rules_GuangDong.SHAN_TOU_QI_DUI_ALL) ? 20
				: smallHu ? 4 : 2, getMutexScoreTypes(PlayType.HU_SHUANG_HAO_HUA_QI_DUI, rules)));

			// 6. 三豪华七小对：7小对中，有3个4张牌一样 +30
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_SanHaoHuaQiDui.class, ScoreCalculateType.TOTAL_ADD, rules.contains(Rules_GuangDong.SHAN_TOU_QI_DUI_ALL) ? 30
				: smallHu ? 4 : 2, getMutexScoreTypes(PlayType.HU_SAN_HAO_HUA_QI_DUI, rules)));

			// 8、一条龙，6分。（胡牌牌型中有同花色的1-9各一张的至少九张牌）
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_YiTiaoLong_ShanWei.class,
				ScoreCalculateType.TOTAL_ADD,
				rules.contains(Rules_GuangDong.SHAN_TOU_YI_TIAO_LONG) ? 6 : smallHu ? 4 : 2,
				getMutexScoreTypes(PlayType.HU_YI_TIAO_LONG, rules)));

			// 9、大三元，20分。（胡牌牌型中有箭牌（中发白）的三个刻子或杠）
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_DaSanYuan.class, ScoreCalculateType.TOTAL_ADD, rules.contains(Rules_GuangDong.SHAN_TOU_DA_SAN_YUAN) ? 20
				: smallHu ? 4 : 2, getMutexScoreTypes(PlayType.HU_DA_SAN_YUAN, rules)));

			// 10、小三元，10分。（胡牌牌型中有箭牌的两个刻子或杠+一个对子，例如123条+555筒+中中中中+发发+白白白）
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_XiaoSanYuan.class, ScoreCalculateType.TOTAL_ADD, rules.contains(Rules_GuangDong.SHAN_TOU_XIAO_SAN_YUAN) ? 10
				: smallHu ? 4 : 2, getMutexScoreTypes(PlayType.HU_XIAO_SAN_YUAN, rules)));

			// 11、大四喜，20分。（胡牌牌型中有风牌（东南西北）的四个刻子或杠，不再计算碰碰胡得分）
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_DaSiXi.class, ScoreCalculateType.TOTAL_ADD, rules.contains(Rules_GuangDong.SHAN_TOU_DA_SI_XI) ? 20
				: smallHu ? 4 : 2, getMutexScoreTypes(PlayType.HU_DA_SI_XI, rules)));

			// 12、小四喜，10分。（胡牌牌型中有风牌的三个刻子或杠+一个对子）
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_XiaoSiXi.class, ScoreCalculateType.TOTAL_ADD, rules.contains(Rules_GuangDong.SHAN_TOU_XIAO_SI_XI) ? 10
				: smallHu ? 4 : 2, getMutexScoreTypes(PlayType.HU_XIAO_SI_XI, rules)));

			// 四鬼胡牌
			if (rules.contains(Rules_GuangDong.MASTER_CARD_FORE_HU))
			{
				if (rules.contains(Rules_GuangDong.MASTER_CARD_DOUBLE_SCORE))
					arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_4MasterCard.class, ScoreCalculateType.TOTAL_ADD, 4, new int[] {PlayType.HU_PING_HU,
						PlayType.HU_HUN_YI_SE, PlayType.HU_QING_YI_SE, PlayType.HU_ZI_YI_SE, PlayType.HU_HUN_YAO_JIU, PlayType.HU_SI_GANG, PlayType.HU_PENG_PENG_HU, PlayType.HU_QI_DUI,
						PlayType.HU_HAO_HUA_QI_DUI, PlayType.HU_SHUANG_HAO_HUA_QI_DUI, PlayType.HU_SAN_HAO_HUA_QI_DUI, PlayType.HU_SHI_SAN_YAO, PlayType.HU_TIAN_HU, PlayType.HU_DI_HU,
						PlayType.HU_DA_SAN_YUAN, PlayType.HU_XIAO_SAN_YUAN, PlayType.HU_DA_SI_XI, PlayType.HU_XIAO_SI_XI, PlayType.HU_YI_TIAO_LONG}));
				else
					arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_4MasterCard.class, ScoreCalculateType.TOTAL_ADD, 2, new int[] {PlayType.HU_PING_HU,
						PlayType.HU_HUN_YI_SE, PlayType.HU_QING_YI_SE, PlayType.HU_ZI_YI_SE, PlayType.HU_HUN_YAO_JIU, PlayType.HU_SI_GANG, PlayType.HU_PENG_PENG_HU, PlayType.HU_QI_DUI,
						PlayType.HU_HAO_HUA_QI_DUI, PlayType.HU_SHUANG_HAO_HUA_QI_DUI, PlayType.HU_SAN_HAO_HUA_QI_DUI, PlayType.HU_SHI_SAN_YAO, PlayType.HU_TIAN_HU, PlayType.HU_DI_HU,
						PlayType.HU_DA_SAN_YUAN, PlayType.HU_XIAO_SAN_YUAN, PlayType.HU_DA_SI_XI, PlayType.HU_XIAO_SI_XI, PlayType.HU_YI_TIAO_LONG}));
			}

			// 4、 十八罗汉：四个杠配合1个对子组成 +36
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_SiGang.class, ScoreCalculateType.TOTAL_ADD, rules.contains(Rules_GuangDong.SHAN_TOU_SHI_BA_LUO_HAN) ? 36
				: smallHu ? 4 : 2, getMutexScoreTypes(PlayType.HU_SI_GANG, rules)));

			// 5、 一九胡：整手牌都有 1，9加字牌组成 +10
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HunYaoJiu.class, ScoreCalculateType.TOTAL_ADD, rules.contains(Rules_GuangDong.SHAN_TOU_YAO_JIU) ? 10
				: smallHu ? 4 : 2, getMutexScoreTypes(PlayType.HU_HUN_YAO_JIU, rules)));

			// 其他:
			if (rules.contains(Rules_GuangDong.SHAN_TOU_TIAN_DI))
			{
				// 1. 天胡：庄家起手14张就胡牌+40，不管什么牌型
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_TianHu.class, ScoreCalculateType.TOTAL_ADD, 40, new int[] {PlayType.HU_PING_HU, PlayType.HU_HUN_YI_SE,
					PlayType.HU_QING_YI_SE, PlayType.HU_ZI_YI_SE, PlayType.HU_SI_GANG, PlayType.HU_PENG_PENG_HU, PlayType.HU_QI_DUI, PlayType.HU_HUN_YAO_JIU, PlayType.HU_HAO_HUA_QI_DUI,
					PlayType.HU_SHUANG_HAO_HUA_QI_DUI, PlayType.HU_SAN_HAO_HUA_QI_DUI, PlayType.HU_SHI_SAN_YAO, PlayType.HU_DA_SAN_YUAN, PlayType.HU_XIAO_SAN_YUAN, PlayType.HU_DA_SI_XI,
					PlayType.HU_XIAO_SI_XI, PlayType.HU_YI_TIAO_LONG}));

				// 2. 地胡：闲家起手13张，自己摸的第一张牌就胡，或者是在自己摸牌前，就点胡 +20 不管什么牌型
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_DiHu_ShanWei.class, ScoreCalculateType.TOTAL_ADD, 20, new int[] {PlayType.HU_PING_HU,
					PlayType.HU_HUN_YI_SE, PlayType.HU_QING_YI_SE, PlayType.HU_ZI_YI_SE, PlayType.HU_HUN_YAO_JIU, PlayType.HU_SI_GANG, PlayType.HU_PENG_PENG_HU, PlayType.HU_QI_DUI,
					PlayType.HU_HAO_HUA_QI_DUI, PlayType.HU_SHUANG_HAO_HUA_QI_DUI, PlayType.HU_SAN_HAO_HUA_QI_DUI, PlayType.HU_SHI_SAN_YAO, PlayType.HU_DA_SAN_YUAN, PlayType.HU_XIAO_SAN_YUAN,
					PlayType.HU_DA_SI_XI, PlayType.HU_XIAO_SI_XI, PlayType.HU_YI_TIAO_LONG}));
			}
			else
			{
				// 1. 天胡：庄家起手14张就胡牌+40，不管什么牌型
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_TianHu.class, ScoreCalculateType.TOTAL_ADD, smallHu ? 4 : 2, getMutexScoreTypes(PlayType.HU_TIAN_HU, rules)));

				// 2. 地胡：闲家起手13张，自己摸的第一张牌就胡，或者是在自己摸牌前，就点胡 +20 不管什么牌型
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_DiHu_ShanWei.class,
					ScoreCalculateType.TOTAL_ADD,
					smallHu ? 4 : 2,
					getMutexScoreTypes(PlayType.HU_DI_HU, rules)));
			}

			// 3. 杠爆/杠上花：杠牌后，抓到牌就自摸所胡牌型*2
			if (rules.contains(Rules_GuangDong.GANG_BAO_DOUBLE))
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_GangShangHua.class, ScoreCalculateType.TOTAL_MULTI, 2));

			// 吃杠杠爆全包
			if (rules.contains(Rules_GuangDong.CHI_GANG_GANG_BAO_QUAN_BAO))
			{
				if (rules.contains(Rules_GuangDong.GAME_PLAY_THREE_PLAYER))
					arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ChiGangGangBao.class, ScoreCalculateType.TOTAL_MULTI_2ND, 2));
				else
					arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ChiGangGangBao.class, ScoreCalculateType.TOTAL_MULTI_2ND, 3));
			}
			// 4. 抢杠胡：补杠的时候，这张牌正好放炮所胡牌型*3
			if (rules.contains(Rules_GuangDong.GAME_PLAY_THREE_PLAYER))
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QiangGangHu.class, ScoreCalculateType.TOTAL_MULTI_2ND, 2));
			else
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QiangGangHu.class, ScoreCalculateType.TOTAL_MULTI_2ND, 3));

			// 5. 海底捞月：最后一张牌自摸了所胡牌型*2
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HaiLao_ShanTou.class, ScoreCalculateType.TOTAL_MULTI, 2));

			// 无鬼加倍
			if (rules.contains(Rules_GuangDong.MASTER_CARD_NONE_DOUBLE))
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_NoMaster.class, ScoreCalculateType.TOTAL_MULTI, 2));

			createContext.element(HuScoreProcessor.KEY_SCORE_CHECKERS, arrayContext);
		}

		context.element(BattleHelper.KEY_HU_SCORE_PROCESSOR, createContext);
	}

	/**
	 * @param checkType 需要检查互斥的类型
	 * @param rules 客户端传过来的规则
	 * @return 返回一个checkType的互斥数组
	 */
	private int[] getMutexScoreTypes(int checkType, List<Integer> rules)
	{
		// 存放 @param checkType的互斥类型的list
		List<Integer> mutexScoreTypeList = new ArrayList<Integer>();

		// 所有牌型互斥平胡
		mutexScoreTypeList.add(PlayType.HU_PING_HU);

		// 七对自己的互斥
		{
			if (checkType == PlayType.HU_HAO_HUA_QI_DUI)
				mutexScoreTypeList.add(PlayType.HU_QI_DUI);
			if (checkType == PlayType.HU_SHUANG_HAO_HUA_QI_DUI)
			{
				mutexScoreTypeList.add(PlayType.HU_QI_DUI);
				mutexScoreTypeList.add(PlayType.HU_HAO_HUA_QI_DUI);
			}
			if (checkType == PlayType.HU_SAN_HAO_HUA_QI_DUI)
			{
				mutexScoreTypeList.add(PlayType.HU_QI_DUI);
				mutexScoreTypeList.add(PlayType.HU_HAO_HUA_QI_DUI);
				mutexScoreTypeList.add(PlayType.HU_SHUANG_HAO_HUA_QI_DUI);
			}
		}

		// 大四喜和十八罗汉互斥碰碰胡
		if (checkType == PlayType.HU_DA_SI_XI || checkType == PlayType.HU_SI_GANG)
			mutexScoreTypeList.add(PlayType.HU_PENG_PENG_HU);

		// cardType_RulesMap是个LinkedHashMap，keySet是有序的
		for (int type : cardType_RulesMap.keySet())
		{
			// 如果牌型到检测的牌型了,并且检测的牌型没有被选择，就退出循环，这样可以让小分不会互斥大分
			if (type == checkType && rules.contains(cardType_RulesMap.get(type)) == false)
				break;

			// 如果规则和包含这个分数类型所对应的规则，并且没有添加进 @param mutexScoreTypeList中（防止重复），就添加进 @param mutexScoreTypeList
			if (rules.contains(cardType_RulesMap.get(type)) == false && !mutexScoreTypeList.contains(type))
				mutexScoreTypeList.add(type);
		}

		Macro.AssetTrue(mutexScoreTypeList.isEmpty(), "配置牌型错误");

		int[] mutexScoreTypeArray = new int[mutexScoreTypeList.size()];
		// 返回一个int数组，所以转换成int数组
		for (int i = 0; i < mutexScoreTypeArray.length; ++i)
		{
			mutexScoreTypeArray[i] = mutexScoreTypeList.get(i);
		}

		return mutexScoreTypeArray;
	}
}
