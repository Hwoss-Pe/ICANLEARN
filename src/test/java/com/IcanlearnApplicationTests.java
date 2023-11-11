package com;

import com.mapper.ForumMapper;
import com.pojo.Forum;
import com.pojo.ForumPost;
import com.pojo.ForumPostCollect;
import com.utils.HanLPUtils;
import com.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

//@SpringBootTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class IcanlearnApplicationTests {


//    @Autowired
//    private final RedisUtil redisUtil;
//
//    @Autowired
//    public IcanlearnApplicationTests(RedisUtil redisUtil) {
//        this.redisUtil = redisUtil;
//    }
//
//    @Autowired
//    ForumMapper forumMapper;

    @Autowired
    private HanLPUtils hanLPUtils;

    @Test
    public void Test() {

        String text = "职业规划是每个人都应该重视的事情，它关乎我们的未来和生活。下面是一篇关于职业规划的文章：  标题：明确目标，精心规划——走向职业成功之路 在当今这个快速变化的社会，职业规划已经成为了我们生活中不可或缺的一部分。无论你是刚刚步入职场的新人，还是已经在职场摸爬滚打多年的老手，都需要有一个清晰而实际的职业规划，以引导你在职业发展的道路上稳步前行。 首先，我们需要明确自己的职业目标。目标是指南针，它能够帮助我们确定方向，让我们知道自己应该朝哪个方向努力。你的目标可以是长期的，比如你希望在10年后成为公司的高级管理人员；也可以是短期的，比如你希望在接下来的一年里提升自己的技能水平。无论目标是什么，关键是要清晰、具体，并且符合你的实际情况。 其次，我们需要制定详细的行动计划。有了目标之后，就需要考虑如何实现这个目标。你需要确定哪些步骤是必要的，哪些资源是可利用的，以及如何有效地管理自己的时间和精力。在制定计划的过程中，你可能需要寻求他人的建议，比如你的导师、同事或者职业规划顾问。 然后，我们需要持续地评估和调整我们的职业规划。职业规划不是一次性的事情，而是一个持续的过程。随着时间的推移，我们的情况会发生变化，我们的目标也可能需要调整。因此，我们应该定期评估自己的进度，看看是否还在正确的道路上，如果有必要，就要及时调整我们的计划。 最后，我们需要有决心和毅力来实现我们的职业规划。职业发展的道路充满了挑战和困难，我们需要有坚定的决心和不屈的毅力，才能够克服这些挑战，实现我们的目标。总的来说，职业规划是一个系统的过程，需要我们明确目标，制定计划，持续评估和调整，并且有决心和毅力来实现我们的目标。只有这样，我们才能在职业发展的道路上走得更远，实现自己的职业理想。";
        String text1 = "职业规划是指个人根据自身的兴趣、能力和价值观，对未来职业生涯进行系统的规划和设计。它包括对个人职业目标的确定、职业路径的选择、职业技能的培养和职业发展的管理。\n" +
                "\n" +
                "职业规划对于个人来说非常重要。它可以帮助个人明确职业目标，选择合适的职业道路，并为实现职业目标做好准备。职业规划还可以帮助个人在职业生涯中不断进步，实现自我价值。\n" +
                "\n" +
                "如果你正在考虑自己的职业规划，那么不妨从以下几个方面入手：\n" +
                "\n" +
                "确定你的兴趣和能力。\n" +
                "了解你的价值观。\n" +
                "确定你的职业目标。\n" +
                "选择合适的职业道路。\n" +
                "培养你的职业技能。\n" +
                "管理你的职业发展。\n" +
                "职业规划是一个持续的过程。你需要不断地根据自己的情况和环境的变化来调整你的职业规划。如果你能够坚持不懈地进行职业规划，那么你将能够在职业生涯中取得更大的成功。";

        String text2 = "职业规划是人生规划的重要组成部分，它是个人根据自己的兴趣、能力、价值观和社会需求，对自己的职业生涯进行系统的规划和设计。职业规划有助于个人明确职业目标，选择合适的职业道路，并为实现职业目标做好准备。\n" +
                "\n" +
                "职业规划可以分为短期规划、中期规划和长期规划。短期规划是指个人对未来一到两年内的职业发展目标和计划，中期规划是指个人对未来三到五年内的职业发展目标和计划，长期规划是指个人对未来五年以上";

        String text3 = "为什么大多数程序员都不擅长英语？\n" +
                "因为他们把所有的精力都用在了学习计算机语言上了。";

        String text4 = "上次写过《TextRank算法提取关键词的Java实现》，这次用TextRank实现文章的自动摘要。\n" +
                "\n" +
                "所谓自动摘要，就是从文章中自动抽取关键句。何谓关键句？人类的理解是能够概括文章中心的句子，机器的理解只能模拟人类的理解，即拟定一个权重的评分标准，给每个句子打分，之后给出排名靠前的几个句子。";

        String text5 = "算法可大致分为基本算法、数据结构的算法、数论算法、计算几何的算法、图的算法、动态规划以及数值分析、加密算法、排序算法、检索算法、随机化算法、并行算法、厄米变形模型、随机森林算法。\n" +
                "\n" +
                "算法可以宽泛的分为三类，\n" +
                "\n" +
                "一，有限的确定性算法，这类算法在有限的一段时间内终止。他们可能要花很长时间来执行指定的任务，但仍将在一定的时间内终止。这类算法得出的结果常取决于输入值。\n" +
                "\n" +
                "二，有限的非确定算法，这类算法在有限的时间内终止。然而，对于一个（或一些）给定的数值，算法的结果并不是唯一的或确定的。\n" +
                "\n" +
                "三，无限的算法，是那些由于没有定义终止定义条件，或定义的条件无法由输入的数据满足而不终止运行的算法。通常，无限算法的产生是由于未能确定的定义终止条件。";

        Set<String> list = hanLPUtils.extractHighFrequencyNouns(text, 10);

        for (String s : list) {
            System.out.println(s);
        }


//
//        ForumPost forumPost = forumMapper.selectForumPostByPostId("1");
//        System.out.println(forumPost);
//        List<ForumPostCollect> list = new ArrayList<>();
//        ForumPostCollect forumPostCollect1 =new ForumPostCollect();
//        ForumPostCollect forumPostCollect2 =new ForumPostCollect();
//
//        forumPostCollect1.setPostId(1);
//        forumPostCollect2.setPostId(3);
//
//        list.add(forumPostCollect1);
//        list.add(forumPostCollect2);
//


//        ForumPost forumPost = new ForumPost();
//        forumPost.setTitle("111111");
//        forumPost.setPublisherId(123);
//
//        List<String> images = new ArrayList<>();
//        images.add("111");
//        images.add("222");
//        images.add("333");
//        forumPost.setLabels(images);
//        forumMapper.insertForumPost(forumPost);

//
//        redisUtil.set(RedisConstant.AUTO_INCREMENT_ID,1,RedisConstant.INCREMENT_ID_EXPIRE_TIME);
//        Integer o = (Integer) redisUtil.get(RedisConstant.AUTO_INCREMENT_ID);
//        System.out.println(o);
//        redisUtil.incr(RedisConstant.AUTO_INCREMENT_ID,1);
//        Integer o1 = (Integer) redisUtil.get(RedisConstant.AUTO_INCREMENT_ID);
//        System.out.println(o1);
    }
}
