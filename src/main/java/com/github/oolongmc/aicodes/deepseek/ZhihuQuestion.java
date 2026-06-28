package com.github.oolongmc.aicodes.deepseek;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 用于承接知乎问题页面的API返回数据。
 * 懒得写！所以让DeepSeek写的。五百多行人写得累死。
 */
public class ZhihuQuestion {
    
    
    /**
     * 错误信息类，用于表示请求过程中出现的错误。
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Error {
        public int code = 0;
        public String message = null;
    }
    
    // --- 成功响应之后的顶层字段 ---
    public Error error = null;
    /** 已读取并过滤掉的条目数量 */
    public int finished_reading_filtered_count;
    /** 回答卡片数组 */
    public Question[] data;
    /** 会话信息 */
    public Session session;
    /** 分页信息 */
    public Paging paging;

    /**
     * 单个问题卡片类，对应JSON中的每一个回答卡片。
     */
    public static class Question {
        /** 卡片类型，例如 "question_feed_card" */
        public String type;
        /** 目标内容类型，例如 "answer" */
        public String target_type;
        /** 目标内容的详细数据 */
        public Target target;
        /** 是否跳过计数 */
        public boolean skip_count;
        /** 位置信息 */
        public int position;
        /** 游标值，用于分页加载更多 */
        public String cursor;
        /** 是否需要强制跳转到原生页面 */
        public boolean is_jump_native;
    }

    // --- 以下是 Target 及其子类的定义 ---

    /**
     * 回答的目标内容，包含了答案、作者、问题等详细信息。
     */
    public static class Target {
        /** 是否允许段落交互 */
        public int allow_segment_interaction;
        /** 标注详情（可选字段，如无可靠信息来源标注） */
        public AnnotationDetail annotation_detail;
        /** 回答类型，例如 "normal" */
        public String answer_type;
        /** 附加信息，通常是Base64编码的数据 */
        public String attached_info;
        /** 作者信息 */
        public Author author;
        /** 业务类型 */
        public String business_type;
        /** 评论数 */
        public int comment_count;
        /** 内容标记 */
        public ContentMark content_mark;
        /** 内容是否需要被截断 */
        public boolean content_need_truncated;
        /** 创建时间戳（秒） */
        public long created_time;
        /** 装饰标签数组 */
        public Object[] decorative_labels;
        /** 回答摘要 */
        public String excerpt;
        /** 额外信息 */
        public String extras;
        /** 收藏夹计数 */
        public int favlists_count;
        /** 点击"阅读更多"时是否强制登录 */
        public boolean force_login_when_click_read_more;
        /** 是否有发布中的草稿 */
        public boolean has_publishing_draft;
        /** 答案ID */
        public String id;
        /** 是否被折叠 */
        public boolean is_collapsed;
        /** 是否可复制 */
        public boolean is_copyable;
        /** 是否需要跳转到原生页面 */
        public boolean is_jump_native;
        /** 是否是自己的回答 */
        public boolean is_mine;
        /** 是否是导航器 */
        public boolean is_navigator;
        /** 是否是置顶回答 */
        public boolean is_sticky;
        /** 是否可见 */
        public boolean is_visible;
        /** 矩阵提示文本 */
        public String matrix_tips;
        /** 导航投票信息 */
        public boolean navigator_vote;
        /** 关联的问题信息 */
        public QuestionInfo question;
        /** 用户的互动反应（点赞、收藏等） */
        public Reaction reaction;
        /** 反应指令 */
        public ReactionInstruction reaction_instruction;
        /** 关系信息（是否关注、投票等） */
        public Relationship relationship;
        /** 相关信息 */
        public RelevantInfo relevant_info;
        /** 段落信息数组 */
        public SegmentInfo[] segment_infos;
        /** 置顶信息 */
        public String sticky_info;
        /** 感谢数 */
        public int thanks_count;
        /** 缩略图信息 */
        public ThumbnailInfo thumbnail_info;
        /** 内容类型，例如 "answer" */
        public String type;
        /** 更新时间戳（秒） */
        public long updated_time;
        /** 答案URL */
        public String url;
        /** 是否仅作者可见 */
        public boolean visible_only_to_author;
        /** 下一步投票操作 */
        public String vote_next_step;
        /** 赞同数 */
        public int voteup_count;
        /** 推荐信息数组（可选字段） */
        public Endorsement[] endorsements;
    }

    /**
     * 标注详情类，用于表示回答上的标注信息（如"无可靠信息来源"）。
     */
    public static class AnnotationDetail {
        /** 标注详情文本 */
        public String detail;
        /** 标注ID */
        public int id;
        /** 标注原因代码 */
        public int reason;
        /** 标注原因描述 */
        public String reason_description;
        /** 是否在外层显示 */
        public boolean show_outer;
        /** 标注类型，例如 "weak" */
        public String type;
    }

    /**
     * 作者信息类
     */
    public static class Author {
        /** 头像URL */
        public String avatar_url;
        /** 头像URL模板 */
        public String avatar_url_template;
        /** 徽章数组 */
        public Object[] badge;
        /** 徽章V2信息 */
        public BadgeV2 badge_v2;
        /** 暴露的勋章信息 */
        public ExposedMedal exposed_medal;
        /** 关注者数（可选） */
        public Integer follower_count;
        /** 性别：0-未知，1-男，-1-女 */
        public int gender;
        /** 个人签名 */
        public String headline;
        /** 用户唯一标识 */
        public String id;
        /** 是否是广告主 */
        public boolean is_advertiser;
        /** 是否被屏蔽（可选） */
        public Boolean is_blocked;
        /** 是否正在屏蔽他人（可选） */
        public Boolean is_blocking;
        /** 是否是名人（可选） */
        public Boolean is_celebrity;
        /** 是否已被关注 */
        public boolean is_followed;
        /** 是否正在关注 */
        public boolean is_following;
        /** 是否是机构号 */
        public boolean is_org;
        /** 是否是隐私用户 */
        public boolean is_privacy;
        /** 用户名 */
        public String name;
        /** 用户类型，例如 "people" */
        public String type;
        /** 用户API URL */
        public String url;
        /** 用户URL Token */
        public String url_token;
        /** 用户类型 */
        public String user_type;
        /** VIP信息（可选） */
        public VipInfo vip_info;
        /** KVIP信息（可选），与vip_info结构相同 */
        public VipInfo kvip_info;  // ← 添加这个字段
    }

    /**
     * 徽章V2信息
     */
    public static class BadgeV2 {
        /** 详细徽章数组 */
        public DetailBadge[] detail_badges;
        /** 图标URL */
        public String icon;
        /** 合并后的徽章数组 */
        public MergedBadge[] merged_badges;
        /** 夜间模式图标URL */
        public String night_icon;
        /** 标题 */
        public String title;
    }

    /**
     * 详细徽章
     */
    public static class DetailBadge {
        /** 描述 */
        public String description;
        /** 详细类型 */
        public String detail_type;
        /** 图标URL */
        public String icon;
        /** 夜间模式图标URL */
        public String night_icon;
        /** 来源数组 */
        public Object[] sources;
        /** 标题 */
        public String title;
        /** 类型 */
        public String type;
        /** 链接URL */
        public String url;
    }

    /**
     * 合并后的徽章
     */
    public static class MergedBadge {
        /** 描述 */
        public String description;
        /** 详细类型 */
        public String detail_type;
        /** 图标URL */
        public String icon;
        /** 夜间模式图标URL */
        public String night_icon;
        /** 来源数组 */
        public Object[] sources;
        /** 标题 */
        public String title;
        /** 类型 */
        public String type;
        /** 链接URL */
        public String url;
    }

    /**
     * 暴露的勋章信息
     */
    public static class ExposedMedal {
        /** 勋章头像URL */
        public String avatar_url;
        /** 勋章描述 */
        public String description;
        /** 勋章头像边框 */
        public String medal_avatar_frame;
        /** 勋章ID */
        public String medal_id;
        /** 勋章名称 */
        public String medal_name;
        /** 小尺寸头像URL */
        public String mini_avatar_url;
    }

    /**
     * VIP信息
     */
    public static class VipInfo {
        /** 是否是VIP */
        public boolean is_vip;
        /** VIP图标 */
        public VipIcon vip_icon;
        /** KVIP购买链接（kvip_info特有） */
        public String target_url;
    }

    /**
     * VIP图标
     */
    public static class VipIcon {
        /** 夜间模式URL */
        public String night_mode_url;
        /** 普通URL */
        public String url;
    }

    /**
     * 内容标记
     */
    public static class ContentMark {}

    /**
     * 问题信息
     */
    public static class QuestionInfo {
        /** 创建时间戳 */
        public long created;
        /** 问题ID */
        public String id;
        /** 问题类型 */
        public String question_type;
        /** 关系（可能为null） */
        public Object relationship;
        /** 问题标题 */
        public String title;
        /** 类型 */
        public String type;
        /** 更新时间戳 */
        public long updated_time;
        /** 问题URL */
        public String url;
    }

    /**
     * 用户互动反应
     */
    public static class Reaction {
        /** 关系信息 */
        public Relation relation;
        /** 统计数据 */
        public Statistics statistics;
    }

    /**
     * 关系信息
     */
    public static class Relation {
        /** 是否收藏 */
        public boolean faved;
        /** 是否点赞 */
        public boolean liked;
    }

    /**
     * 统计数据
     */
    public static class Statistics {
        /** 反对数 */
        public int down_vote_count;
        /** 收藏数 */
        public int favorites;
        /** 点赞数 */
        public int like_count;
    }

    /**
     * 反应指令
     */
    public static class ReactionInstruction {}

    /**
     * 关系信息（与Reaction中的不同）
     */
    public static class Relationship {
        /** 是否被收藏 */
        public boolean is_favorited;
        /** 点赞过的关注者数组 */
        public Object[] upvoted_followees;
        /** 投票状态：0-未投票，1-已赞，-1-已踩 */
        public int voting;
    }

    /**
     * 相关信息
     */
    public static class RelevantInfo {
        /** 是否相关 */
        public boolean is_relevant;
        /** 相关文本 */
        public String relevant_text;
        /** 相关类型 */
        public String relevant_type;
    }

    /**
     * 段落信息
     */
    public static class SegmentInfo {
        /** 标记数组 */
        public Mark[] marks;
        /** 段落ID */
        public String pid;
        /** 段落文本 */
        public String text;
    }

    /**
     * 标记
     */
    public static class Mark {
        /** 结束索引 */
        public int end_index;
        /** 段信息 */
        public SegInfo seg_info;
        /** 开始索引 */
        public int start_index;
    }

    /**
     * 段信息
     */
    public static class SegInfo {
        /** 评论数 */
        public int comment_count;
        /** 是否被点赞 */
        public boolean is_like;
        /** 是否是跨度 */
        public boolean is_span;
        /** 点赞数 */
        public int like_count;
        /** 我的评论数 */
        public int my_comment_count;
        /** 段ID数组 */
        public String[] seg_ids;
    }

    /**
     * 缩略图信息
     */
    public static class ThumbnailInfo {
        /** 缩略图数量 */
        public int count;
        /** 缩略图数组 */
        public Thumbnail[] thumbnails;
        /** 类型 */
        public String type;
    }

    /**
     * 缩略图
     */
    public static class Thumbnail {
        /** 高度 */
        public int height;
        /** 令牌 */
        public String token;
        /** 类型 */
        public String type;
        /** URL */
        public String url;
        /** 宽度 */
        public int width;
    }

    /**
     * 推荐信息
     */
    public static class Endorsement {
        /** 操作URL */
        public String action_url;
        /** 背景颜色 */
        public BackgroundColor background_color;
        /** 元素数组 */
        public Element[] elements;
        /** 子元素数组 */
        public SubElement[] sub_elements;
        /** 子元素类型 */
        public String sub_elements_type;
        /** ZA信息 */
        public Za za;
    }

    /**
     * 子元素
     */
    public static class SubElement {
        /** 操作URL */
        public String action_url;
        /** 元素数组 */
        public Element[] elements;
    }

    /**
     * 背景颜色
     */
    public static class BackgroundColor {
        /** 透明度 */
        public double alpha;
        /** 颜色组 */
        public String group;
    }

    /**
     * 元素
     */
    public static class Element {
        /** 高度 */
        public int height;
        /** 图片颜色 */
        public ImageColor image_color;
        /** 图片键 */
        public String image_key;
        /** 选中时的图片键 */
        public String selected_image_key;
        /** 类型 */
        public String type;
        /** 宽度 */
        public int width;
        /** 内容文本 */
        public String content;
        /** 字体颜色 */
        public FontColor font_color;
        /** 字体大小 */
        public int font_size;
        /** 是否加粗 */
        public boolean is_bold;
        /** 最大行数 */
        public int max_line;
    }

    /**
     * 图片颜色
     */
    public static class ImageColor {
        /** 透明度 */
        public double alpha;
        /** 颜色组 */
        public String group;
    }

    /**
     * 字体颜色
     */
    public static class FontColor {
        /** 透明度 */
        public double alpha;
        /** 颜色组 */
        public String group;
    }

    /**
     * ZA信息
     */
    public static class Za {
        /** 块文本 */
        public String block_text;
        /** 文本 */
        public String text;
        /** 类型 */
        public String type;
    }

    /**
     * 会话信息
     */
    public static class Session {
        /** 会话ID */
        public String id;
    }

    /**
     * 分页信息
     */
    public static class Paging {
        /** 当前页码 */
        public int page;
        /** 是否已到末尾 */
        public boolean is_end;
        /** 下一页的URL */
        public String next = null;
        /** 是否需要强制登录 */
        public boolean need_force_login;
    }
}