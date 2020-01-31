Main 
Posts
article_1.md/ article_2.md/ article_3.md
About 
about.md
메인 페이지에서 'Posts', 'About' 두 가지 메뉴에 접근할 수 있고 About은 단일 Markdown 파일 하나가 연결되어 있음. 'Posts' 메뉴에는 여러 Article(Markdown)이 있다. 'Posts' 메뉴의 글은 Markdown 파일명에 입력한 날짜를 기준으로 정렬된다. 이런 형태는 Jekyll의 기본 구조라고 볼 수 있고 이를 _post라고 부른다. 
_config.yml 
환경설정 정보를 담고 있음. head에 넣는 메타 정보와 비슷한 정보를 담기도 하고 baseurl url 등도 설정할 수 있음
_drafts
아직 게시하지 않은, 날짜 정보가 없는 Post를 보관할 수 있는 디렉터리이다.
_includes
재사용할 수 있는 부분적으로 만들어진 html을 보관할 수 있는 폴더다. 예를 들어 header나 footer는 모든 곳에서 반복적으로 사용하기 때문에 include 폴더에 만들어놓고 가져다쓰면 편하다. liquid 태그로 _include 안에 html을 소환할 수 있음.
_layouts
defaults.html은 최상위 Jekyll blog 구성을 담고 있는 파일이다. _include 폴더 안에 부분적인 html들이 소환되어 있다. post.html은 Post의 형태를 정의해놓은 html 파일이다. 
_posts
_data
블로그에 사용할 수 있는 데이터를 모아놓을 수 있는 폴더다. .yml, .yaml, .json, .csv일 경우 자동으로 읽어 들여서 site.data 변수를 써서 불러올 수 있음
index.html
블로그에 접속했을 때 제일 먼저 자동으로 보여주는 파일

