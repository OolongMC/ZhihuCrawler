package com.github.oolongmc.application.ZhihuCrawler;


import org.jline.utils.AttributedStyle;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.WCWidth;
import org.jline.utils.InfoCmp;
import org.jline.utils.NonBlockingReader;
import org.jline.terminal.Terminal;
import org.jline.terminal.Attributes;
import org.jline.terminal.Attributes.ControlChar;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * @deprecated 放弃啦，等我学了图形化吧。JLine太难用了，尤其是对于我这个没有电脑的。
 */
public class Reader{
    public static void render(String questionNumber, Terminal terminal) throws IOException{
        boolean is_end = false;
        Window w = new Window(AttributedStyle.WHITE, terminal);
        String a = """
        子曰：“学而时习之，不亦说乎？有朋自远方来，不亦乐乎？人不知而不愠，不亦君子乎？
        有子曰:“其为人也孝弟而好犯上者，鲜矣；不好犯上而好作乱者，未之有也。君子务本，本立而道生。孝弟也者，其为仁之本与!”
        子曰:“巧言令色，鲜矣仁！”
        曾子曰：“吾日三省吾身，为人谋而不忠乎?与朋友交而不信乎?传不习乎?”
        子曰:“道千乘之国，敬事而信，节用而爱人，使民以时。”
        子曰：“弟子入则孝，出则弟，谨而信，泛爱众，而亲仁。行有余力，则以学文。”
        子夏曰：“贤贤易色；事父母，能竭其力；事君，能致其身；与朋友交，言而有信。虽曰未学，吾必谓之学矣。”
        子曰：“君子不重则不威，学则不固。主忠信，无友不如己者，过则勿惮改。”
        曾子曰：“慎终追远，民德归厚矣。”
        子禽问于子贡曰：“夫子至于是邦也，必闻其政，求之与，抑与之与？”子贡曰：“夫子温、良、恭、俭、让以得之。夫子之求之也，其诸异乎人之求之与？”
        子曰：“父在，观其志。父没，观其行；三年无改于父之道，可谓孝矣。”
        有子曰：“礼之用，和为贵。先王之道，斯为美，小大由之。有所不行，知和而和，不以礼节之，亦不可行也。”
        有子曰：“信近于义，言可复也。恭近于礼，远耻辱也。因不失其亲，亦可宗也。”
        子曰：“君子食无求饱，居无求安，敏于事而慎于言，就有道而正焉。可谓好学也已。”
        子曰：“为政以德，譬如北辰，居其所而众星共之。”
        子曰：“《诗》三百，一言以蔽之，曰：‘思无邪’。”
        子曰：“道之以政，齐之以刑，民免而无耻。道之以德，齐之以礼，有耻且格。”
        子曰：“吾十有五而志于学，三十而立，四十而不惑，五十而知天命，六十而耳顺，七十而从心所欲，不逾矩。”
        孟懿子问孝，子曰：“无违。”樊迟御，子告之曰：“孟孙问孝于我，我对曰‘无违’。”樊迟曰：“何谓也？”子曰：“生，事之以礼；死，葬之以礼，祭之以礼。”
        孟武伯问孝。子曰：“父母唯其疾之忧。”
        子游问孝，子曰：“今之孝者，是谓能养。至于犬马，皆能有养。不敬，何以别乎？”
        子夏问孝。子曰：“色难。有事，弟子服其劳；有酒食，先生馔，曾是以为孝乎？”
        子曰：“吾与回言终日，不违，如愚。退而省其私，亦足以发，回也不愚。”
        子曰：“视其所以，观其所由，察其所安，人焉廋哉？人焉廋哉？”
        子曰：“温故而知新，可以为师矣。”
        子曰：“君子不器。”
        子贡问君子。子曰：“先行其言而后从之。”
        子曰：“君子周而不比，小人比而不周。”
        子曰：“学而不思则罔，思而不学则殆。”
        子曰：“攻乎异端，斯害也已！”
        子曰：“由，诲女，知之乎！知之为知之，不知为不知，是知也。”
        子张学干禄。子曰：“多闻阙疑，慎言其余，则寡尤；多见阙殆，慎行其余，则寡悔。言寡尤，行寡悔，禄在其中矣。”
        哀公问曰：“何为则民服？”孔子对曰：“举直错诸枉，则民服；举枉错诸直，则民不服。”
        季康子问：“使民敬、忠以劝，如之何？”子曰：“临之以庄，则敬；孝慈，则忠；举善而教不能，则劝。”
        或谓孔子曰：“子奚不为政？”子曰：“《书》云：‘孝乎惟孝，友于兄弟，施于有政。’是亦为政，奚其为为政？”
        子曰：“人而无信，不知其可也。大车无輗，小车无軏，其何以行之哉？”
        子张问：“十世可知也？”子曰：“殷因于夏礼，所损益，可知也；周因于殷礼，所损益，可知也。其或继周者，虽百世，可知也。”
        子曰：“非其鬼而祭之，谄也；见义不为，无勇也。”
        """;
        String b = """
        三体II:
                直到现在，罗辑也不知道白蓉这要求到底是什么用意，也许连她自己也不知道，现在回想起来，她当时的表情好像有些狡猾，又有些忧郁。
                于是，罗辑开始构思这个人物。他首先想象她的容貌，然后为她设计衣着，接着设想她所处的环境和她周围的人，最后把她放到这个环境中，让她活动和说话，让她生活。很快，这事变得索然无味了，他向白蓉诉说了自己遇到的困境。
                “她好像是一个提线木偶，每个动作和每一句话都来自我的设想，缺少一种生命感。”
                白蓉说：“你的方法不对，你是在作文，不是在创造文学形象。要知道，一个文学人物十分钟的行为，可能是她十年的经历的反映。你不要局限于小说的情节，要去想象她的整个生命，而真正写成文字的，只是冰山的一角。”
                于是罗辑照白蓉说的做了，完全抛开自己要写的内容，去想象她的整个人生，想象她人生中的每一个细节。他想象她在妈妈的怀中吃奶，小嘴使劲吮着，发出满意的唔唔声；想象雨中漫步的她突然收起了伞，享受着和雨丝接触的感觉；想象她追一个在地上滚的红色气球，仅追了一步就摔倒了，看着远去的气球哇哇大哭，完全没有意识到她刚才迈出的是人生的第一步；想象她上小学的第一天，孤独地坐在陌生教室的第三排，从门口和窗子都看不到爸爸妈妈了，就在她要哭出来时，发现邻桌是幼儿园的同学，又高兴得叫起来；想象大学的第一个夜晚，她躺在宿舍的上铺，看着路灯投在天花板上的树影……罗辑想象着她爱吃的每一样东西，想象她的衣橱中每一件衣服的颜色和样式，想象她手机上的小饰物，想象她看的书她的MP4中的音乐她上的网站她喜欢的电影，但从未想象过她用什么化妆品，她不需要化妆品……罗辑像一个时间之上的创造者，同时在她生命中的不同时空编织着她的人生，他渐渐对这种创造产生了兴趣，乐此不疲。
                一天在图书馆，罗辑想象她站在远处的一排书架前看书，他为她选了他最喜欢的那一身衣服，只是为了使她的娇小身材在自己的印象中更清晰一些。突然，她从书上抬起头来，远远地看了他一眼，冲他笑了一下。
                罗辑很奇怪，我没让她笑啊？可那笑容已经留在记忆中，像冰上的水渍，永远擦不掉了。
                真正的转机发生在第二天夜里。这天晚上风雪交加，气温骤降，在温暖的宿舍里，罗辑听着外面狂风怒号，盖住了城市中的其他声音，打在玻璃上的雪花像沙粒般啪啪作响，向外看一眼也只见一片雪尘。这时，城市似乎已经不存在了，这幢教工宿舍楼似乎是孤立在无垠的雪原上。罗辑躺回床上，进入梦乡前突然有了一个想法：这鬼天气，她要是在外面走路该多冷啊。他接着安慰自己：没关系，你不让她在外面她就不在外面了。但这次他的想象失败了，她仍在外面的风雪中行走着，像一株随时都会被寒风吹走的小草，她穿着那件白色的大衣，围着那条红色的围巾，飞扬的雪尘中也只能隐约看到红围巾，像在风雪中挣扎的小火苗。
                罗辑再也不可能入睡了，他起身坐在床上，后来又披衣坐到沙发上，本来想抽烟的，但想起她讨厌烟味，就冲了一杯咖啡慢慢地喝着。他必须等她，外面的寒夜和风雪揪着他的心，他第一次如此心疼一个人，如此想念一个人。
        """;
        String c = """
        As research delves deeper, humanity is discovering that quantum effects are nothing more than surface ripples in the ocean of existence, shadows of the disturbances arising from the deeper laws governing the workings of matter. With these laws beginning to reveal themselves, quantum mechanics' ever-shifting picture of reality is once again stabilizing, deterministic variables once again replacing probabilities. In this new model of the universe, the chains of causality that were thought eliminated have surfaced once more, and clearer than before.
        Pursuit
        In the office were the flags of China and the CPC. There were also two men, one on either side of the broad desk.
        'I know you're very busy, sir, but I must report this. I've honestly never seen anything like it,' said the man in front of the desk. He wore the uniform of a police superintendent second class. He was near fifty, but he stood ramrod-straight, and the lines of his face were hard and vigorous.
        'I know the weight of that last sentence coming from you, Jifeng￼, veteran investigator of thirty years.' The Senior Official looked at the red and blue pencil slowly twirling between his fingers as he spoke, as if all his attention were focused on assessing the merit of its sharpening. He tucked away his gaze like this much of the time. In the years Chen Jifeng had known him, the Senior Official had looked him in the eyes no more than three times. Each time had come at a turning point in Chen's life.
        'Every time we take action, the target escapes one step ahead of us. They know what we're going to do.'
        'Surely you've seen similar things before,' the Senior Official said.
        'If it were simply that, it wouldn't be a big deal, of course. We considered the possibility of an inside job right off.'
        'Knowing your subordinates, I find that rather improbable.'
        'We found that out for ourselves,' Chen said. 'Like you instructed, we've reduced the participants in this case as much as possible. There are only four people in the task force, and only two know the full story. But just in case, I planned to call a meeting of all the members and question them one by one. I told Chenbing to handle it—you know him, the one from the Eleventh Department, very reliable, took care of the business with Song Cheng—and that's when it happened.
        'Don't take this for a joke, sir. What I'm going to say next is the honest truth.' Chen Jifeng laughed a little, as if embarrassed by his own defensiveness. 'Right then, they called. Our target called me on the phone! I heard him say on my cell phone, You don't need this meeting, there's no traitor among you. Less than thirty seconds after I told Chenbing I wanted to call a meeting!'
        The Senior Official's pencil stilled between his fingers.
        'You might be thinking that we were bugged, but that's impossible. I chose the location for the conversation at random to be the middle of a government agency auditorium while it was being used for chorus rehearsals for National Day. We had to talk right into each other's ears to hear.
        'And similar funny business kept happening after that. He called us eight times in total, each time about things we had just said or done. The scariest part is, not only does he hear everything, he sees everything. One time,Chenbing decided to search the target's parents' home. He and the other task force member were just standing up, not even out of the department office, when they got the target's call. You guys have the wrong search warrant, they told them. My parents are careful people. They might think you guys are frauds.Chenbing took out the warrant to check, and sir, he really had taken the wrong one.'
        The Senior Official set the pencil lightly on his desk, waiting in silence for Chen Jifeng to continue, but the latter seemed to have run out of steam. The Senior Official took out a cigarette. Chen Jifeng hurriedly patted at his coat pockets for a lighter, but couldn't find one.
        """;
        int t = 0;
        int tmp = w.render("山东一教师与千名男学生的聊天记录", new AttributedString(a));
        
        for(int i = 0;!is_end;i++){
            //String json = Files.readString(Path.of(config.cookiePath));
            
            
            if(tmp == -1){t = (t + 1)%3;}
            else if(tmp == -2){t = (t + 2)%3;}
            else if(tmp == 0){return;}
            
            switch(t){
                case 0: tmp = w.render("山东一教师与千名男学生的聊天记录", new AttributedString(a));
                case 1: tmp = w.render("三体选段", new AttributedString(b));
                case 2: tmp = w.render("English Test", new AttributedString(c));
            }
        }
        
        
        
        
        
    }
    
    
    
    private static class Window{
        private Terminal terminal;
        private int background;
        private int terminalH;
        private int terminalW;
        private int startLine;
        private String KEY_UP = new String(new byte[]{27, 91, 65});
        private String KEY_DOWN = new String(new byte[]{27, 91, 66});
        private String KEY_LEFT = new String(new byte[]{27, 91, 68});
        private String KEY_RIGHT = new String(new byte[]{27, 91, 67});
        private String KEY_TAB = new String(new byte[]{9});
        private String KEY_ENTER = new String(new byte[]{10});
        
        public Window(int background, Terminal terminal){
            this.terminal = terminal;
            this.background = background;
            this.terminalH = terminal.getHeight();
            this.terminalW = terminal.getWidth();
        }
        
        /**
         * 渲染窗口。
         * @param String title 窗口显示标题。
         * @param AttributedString text 窗口文本。
         * @return <s>用户选择的选项，从1开始，</s>-1为Done(Next),-2为Cancel(Back)，0为退出。
         */
        public int render(String title, AttributedString text/*, String... option*/) throws IOException{
            this.startLine = 0;
            /**
             * 初始化窗口。
             */
            terminal.puts(InfoCmp.Capability.exit_ca_mode);
            terminal.flush();
            
            
            fillScreen(background).print(terminal);
            
            terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);
            terminal.puts(InfoCmp.Capability.exit_insert_mode);
            terminal.flush();
            
            new AttributedString(
                new String("🟦" + title), 
                AttributedStyle.DEFAULT.background(background).foreground(~background)
            ).print(terminal);
            
            updateOption(0);
            terminal.flush();
            
            /**
             * 开始打印文本。
             */
            AttributedString[] texts = splitByWidth(text, terminalW);
            terminal.puts(InfoCmp.Capability.cursor_address, 1, 0);
            updateText(texts);
            /**
             * 按键绑定。
             */
            // 移除Ctrl + C
            Attributes attrs = terminal.getAttributes();
            attrs.setControlChar(ControlChar.VINTR, -1);
            terminal.setAttributes(attrs);
            // 隐藏光标。
            terminal.puts(InfoCmp.Capability.cursor_invisible);
            
            NonBlockingReader reader = terminal.reader();
            
            int nowOption = 0;
            // 0:Back   1:Next   2:Exit
            while(true){
                // 读键，难。
                List<Integer> keyInt = new ArrayList<>();
                long nowTime = System.currentTimeMillis();
                while((System.currentTimeMillis() - nowTime) <= 10){
                    int tmp = reader.read(1);
                    if(tmp > -1){
                        keyInt.add(tmp);
                    }
                }
                byte[] bytes = new byte[keyInt.size()];
                for (int i = 0; i < keyInt.size(); i++) {
                    bytes[i] = keyInt.get(i).byteValue();
                }
                String key = new String(bytes);
                
                
                // 处理按键映射。
                if(key == null){
                    continue;
                }
                
                if(key.startsWith(KEY_UP)&&this.startLine > 0){
                    this.startLine--;
                    updateText(texts);
                }
                else if(key.startsWith(KEY_DOWN)&&this.startLine < texts.length - 1){
                    this.startLine++;
                    updateText(texts);
                }
                else if(key.startsWith(KEY_LEFT)){
                    nowOption = (nowOption + 2) % 3;
                    updateOption(nowOption);
                }
                else if(key.startsWith(KEY_RIGHT)||key.startsWith(KEY_TAB)){
                    nowOption = (nowOption + 1) % 3;
                    updateOption(nowOption);
                }
                else if(key.startsWith(KEY_ENTER)){
                    break;
                }
                terminal.flush();
            }
            
            /**
             * 恢复屏幕。
             */
            attrs.setControlChar(ControlChar.VINTR, 3);
            terminal.setAttributes(attrs);
            terminal.puts(InfoCmp.Capability.enter_ca_mode);
            terminal.puts(InfoCmp.Capability.cursor_normal);
            
            
            switch(nowOption){
                case 0:
                    return -2;
                case 1:
                    return -1;
                default:
                    return 0;
            }
        }
        
        private void updateText(AttributedString[] text){
            for (int r = 1; r <= terminalH - 2; r++) {
                terminal.puts(InfoCmp.Capability.cursor_address, r, 0);
                new AttributedString(" ".repeat(terminalW), AttributedStyle.DEFAULT.background(background)).print(terminal);
            }
            terminal.puts(InfoCmp.Capability.cursor_address, 1, 0);
            int i = this.startLine;
            while(i < Math.min(text.length, startLine + terminalH - 2)){
                text[i].print(terminal);
                terminal.puts(InfoCmp.Capability.cursor_address, 1 + (i - this.startLine), 0);
                i++;
            }
        }
        
        private void updateOption(int nowOption){
            terminal.puts(InfoCmp.Capability.cursor_address, terminalH - 1, 0);
            AttributedStyle now = AttributedStyle.DEFAULT.background(background).foreground(~background);
            AttributedStyle wait = AttributedStyle.DEFAULT.background(~background).foreground(background);
            
            AttributedStringBuilder str = new AttributedStringBuilder();
            if(nowOption == 0){
                str.append(new AttributedString("<Back", now));
            }else{
                str.append(new AttributedString("<Back", wait));
            }
            
            str.append("   ");
            
            if(nowOption == 1){
                str.append(new AttributedString("Next>", now));
            }else{
                str.append(new AttributedString("Next>", wait));
            }
            
            str.append("   ");
            
            if(nowOption == 2){
                str.append(new AttributedString("Exit", now));
            }else{
                str.append(new AttributedString("Exit", wait));
            }
            
            str.print(terminal);
        }
        
        /**
         * 填充整个屏幕背景色喵！
         */
        private AttributedString fillScreen(int backgroundColor){
            AttributedStringBuilder str = new AttributedStringBuilder();
            for(int i = 0; i < terminalH; i++){
                str.append(
                    new String(" ").repeat(terminalW), 
                    AttributedStyle.DEFAULT.background(backgroundColor)
                );
                if(i != terminalH-1){
                    str.append('\n');
                }
            }
            return str.toAttributedString();
        }
        
        /**
         * 按行/指定宽度切割字符串。
         */
        private static AttributedString[] splitByWidth(AttributedString text, int maxWidth){
            if(text == null || text.length() == 0){
                return new AttributedString[0];
            }
            
            List<AttributedString> lines = new ArrayList<>();
            int start = 0;
            int currentWidth = 0;
            int i = 0;
            
            while(i < text.length()){
                int codePoint = text.codePointAt(i);
                char c = text.charAt(i);
                int charCount = Character.charCount(codePoint);
                
                if(c == '\n'){
                    lines.add(text.substring(start, i));
                    start = i + charCount;
                    currentWidth = 0;
                    i += charCount;
                    continue;
                }
                
                int charWidth = WCWidth.wcwidth(codePoint);
                
                if(currentWidth + charWidth > maxWidth){
                    if(currentWidth == 0){
                        lines.add(text.substring(start, i + charCount));
                        start = i + charCount;
                        currentWidth = 0;
                        i += charCount;
                    }else{
                        lines.add(text.substring(start, i));
                        start = i;
                        currentWidth = 0;
                        // i不变，回退
                    }
                }else{
                    currentWidth += charWidth;
                    i += charCount;
                }
            }
            
            if(start < text.length()){
                lines.add(text.substring(start, text.length()));
            }
            
            return lines.toArray(new AttributedString[0]);
        }
    }
}