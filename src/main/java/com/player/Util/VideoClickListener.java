package com.player.Util;

import com.player.MainFrame;
import com.player.Player.MediaPlayer;
import com.player.UI.Bottom.Bottom;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author SDU德布罗煜
 * @Date 2021/5/7 15:23
 * @Version 1.0
 */
public class VideoClickListener extends MouseAdapter {
    private static  boolean flag = false;		//双击事件已执行时置为真
    private static int clickNum = 1;		//指示鼠标点击次数，默认为单击
    @Override
    public void mouseClicked(MouseEvent e) {
        if (Bottom.getFunction().isEnable) {
            VideoClickListener.flag = false;
            if (VideoClickListener.clickNum == 2) {
                //鼠标点击次数为2调用双击事件
                this.mouseClickedTwice();
                //调用完毕clickNum置为1
                VideoClickListener.clickNum = 1;
                VideoClickListener.flag = true;
                return;
            }
            //新建定时器，双击检测间隔为500ms
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                //指示定时器执行次数
                int num = 0;
                @Override
                public void run() {
                    // 双击事件已经执行，取消定时器任务
                    if(VideoClickListener.flag) {
                        num = 0;
                        VideoClickListener.clickNum = 1;
                        this.cancel();
                        return;
                    }
                    //定时器再次执行，调用单击事件，然后取消定时器任务
                    if (num == 1) {
                        mouseClickedOnce();
                        VideoClickListener.flag = true;
                        VideoClickListener.clickNum = 1;
                        num = 0;
                        this.cancel();
                        return;
                    }
                    clickNum++;
                    num++;
                }
            },new Date(), 200);
        }
    }

    private void mouseClickedOnce() {
        // 单击事件
        MediaPlayer player = MediaPlayer.getInstance();
        if (player.isPlaying()) {
            MediaPlayer.pause();
            Bottom.getFunction().playEnd();
        } else {
            try {
                player.go_on();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Bottom.getFunction().playBegin();
        }
        MainFrame.getFrame().requestFocus();
    }

    private void mouseClickedTwice() {
        // 双击事件
        EmbeddedMediaPlayerComponent media = MainFrame.getMedia();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screen.getWidth();
        double height = screen.getHeight();
        if (media.getMediaPlayer().isFullScreen()) {
            MainFrame.getBottom().setVisible(true);
            MainFrame.getList().setVisible(true);
            media.setBounds((int) (width * 0.9 * 0.2), 0, (int) (width * 0.9 * 0.8), (int) (height * 0.9 * 0.8));
            MainFrame.getView().setBounds((int) (width * 0.9 * 0.2), 0, (int) (width * 0.9 * 0.8), (int) (height * 0.9 * 0.8));
            MainFrame.getView().getCover().setBounds(0, 0, (int) (width * 0.9 * 0.8), (int) (height * 0.9 * 0.8));
            MainFrame.getMenu().setVisible(true);
            media.getMediaPlayer().setFullScreen(false);
        } else {
            MainFrame.getBottom().setVisible(false);
            MainFrame.getList().setVisible(false);
            media.setBounds(0,0, (int) width, (int) height);
            MainFrame.getView().setBounds(0,0, (int) width, (int) height);
            MainFrame.getView().getCover().setBounds(0,0, (int) width, (int) height);
            MainFrame.getMenu().setVisible(false);
            media.getMediaPlayer().setFullScreen(true);
        }
        MainFrame.getFrame().requestFocus();
    }
}
