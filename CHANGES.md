[20200206]
1.change OnEmvProcessListener:
Add Rupay contactless (double tap、Long tag) case.

2.EmvTransDataConstrants.CONTACTLESS_PIN_FREE_AMT
Pin Free Amount for contactLess(Amount 2000).



[20200116]

1.change inputText API.

2.cashback support.

3.Add more 9C support (EMVTag9CConstants) .



[20200109-dev]
1.fix bypass value(new byte[0]).
2.add custom tag.
3.change input online pin length( 4 position).



[20200103-dev]
1.Add Grad logcat switch.
2.Add jude:
  if the amount of connect less transactions is more than 2,000. The interface prompts you to swipe or insert a card.
3.EmvTransDataConstrants.FORCE_ONLINE_CALL_PIN:
For online transactions, the terminal must force to enter the password,Please set true.



[20191225]

1、Dukpt：At most 6 groups of keys are supported.
2、Dukpt：increaseKSN API : Generate new PEK and return new KSN.

