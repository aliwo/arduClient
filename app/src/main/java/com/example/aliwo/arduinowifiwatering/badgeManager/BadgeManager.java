package com.example.aliwo.arduinowifiwatering.badgeManager;

import android.content.Context;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by aliwo on 2017-04-15.
 * 여러가지 테스트 결과 Launcher의 icon에 설정된 badge count의 값을 Launcher로부터
 * 받아오는 방법은 없는 듯하며 app 내부적으로 badge 값을 따로 저장하고 있다가
 * 특정 동작시(푸시를 받거나 메시지를 확인하는 경우 등)
 * 해당 값의 수정을 통해 badge number를 갱신하는 로직을 갖고 있는 것으로 보인다.
 출처: http://hypernose.tistory.com/entry/Badge-관련-정리 [예쓰]
 */

public class BadgeManager
{
    Context mcontext;
    public static int badgecount; // TODO: preference에 저장해서 앱 시작할때 초기화 해야 겠네...
    public BadgeManager(Context context)
    {
        mcontext = context;
    }

    public void badgeInit(int num)
    {
        int badgeCount = num;
        ShortcutBadger.applyCount(mcontext, badgeCount); //for 1.1.4+
    }

}
