import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TestJsoup {

    /**
     * @apiNote 获取该路径的页面信息
     * @param url 访问路径
     * @return
     */
    public Document getDocument(String url) {
        try {
            // 5000是设置连接超时时间，单位ms
            return Jsoup.connect(url).timeout(5000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @apiNote 回调方法 通过新的路径获取姓名+邮箱
     * @param url
     * @return
     */
    public void getEmail(String url) {
        // 访问新的标签 拿取数据
        TestJsoup t2 = new TestJsoup();
        Document doc_new = t2.getDocument(url);
        Elements eleme2 = doc_new.select("[class=MsoNormalTable]");
        for (int i = 0; i < eleme2.size(); i++) {
            // 需要导师的邮箱信息和研究方向
            if (i == 0 || i == 3) {
                String text = eleme2.get(i).select("tbody").text();
                System.out.println(text);
            }
        }
    }

    public static void main(String[] args) {
        TestJsoup t = new TestJsoup();
        Document doc = t.getDocument("http://faculty.bjtu.edu.cn/trans/jsfl.html?gid=14");
        // 获取目标HTML代码
        Elements elements1 = doc.select("[class=row]");
        // 个人标签
        Elements elements2 = elements1.select("[class=col-xs-12 col-sm-6 bg_list]");
        for (int i = 0; i < elements2.size(); i++) {
            // 由于朋友只需要教授/副教授和铁路/轨道专业的信息，所以作出以下过滤
            if (elements2.get(i).text().indexOf("教授") != -1 && elements2.get(i).text().indexOf("铁路") != -1) {
                // 获取A标签的href 网址 select 获取到当前A标签 attr href 获取到地址
                String s = "http://faculty.bjtu.edu.cn/" + elements2.get(i).select("a").attr("href");
                // 获取邮箱
                TestJsoup t1 = new TestJsoup();
                t1.getEmail(s);
            } else if (elements2.get(i).text().indexOf("教授") != -1 && elements2.get(i).text().indexOf("轨道") != -1) {
                String s = "http://faculty.bjtu.edu.cn/" + elements2.get(i).select("a").attr("href");
                // System.out.println(s);
                // 获取邮箱
                TestJsoup t1 = new TestJsoup();
                t1.getEmail(s);
            }
        }
    }
}
