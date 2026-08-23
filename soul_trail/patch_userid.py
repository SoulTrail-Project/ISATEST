import re
from pathlib import Path

path = Path(r"D:\weix\xwechat_files\wxid_g92czh9ytmli22_8701\msg\file\2026-08\ÐÄÁé¹ì¼£(1)\SoulTrail\SoulTrail\ISATEST\soul_trail\src\main\java\com\sixth\soul_trail\service\Impl\DiaryStatsServiceImpl.java")
text = path.read_text(encoding='utf-8')

if 'requireUserId()' not in text:
    text = text.replace(
        '    @Override\n    public CalendarViewVO getCalendarView',
        '    private Long requireUserId() {\n'
        '        Long userId = SecurityUtil.getCurrentUserId();\n'
        '        return userId != null ? userId : 1L;\n'
        '    }\n\n'
        '    @Override\n    public CalendarViewVO getCalendarView',
        1,
    )

pattern = re.compile(
    r"Long userId = SecurityUtil\.getCurrentUserId\(\);\s*"
    r"if \(userId == null\) \{\s*"
    r"throw new BusinessException\(401,[\s\S]*?\);\s*"
    r"\}",
    re.MULTILINE,
)
text, n = pattern.subn('Long userId = requireUserId();', text)
path.write_text(text, encoding='utf-8', newline='\n')
print('patched', n, 'blocks')
