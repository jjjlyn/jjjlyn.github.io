`g:vimwiki_conceallevel = 0` 으로 설정해도 계속 특수문자가 숨김처리돼서 매우 스트레스 받았는데, 그 원인은 다른Plugin에있었다.

`Plugin 'Yggdroot/indentLine'`의 conceal_level은 default가 `2`로 설정되어 있다고 함. 그래서 `~/.vimrc` 설정 파일에 두 줄을 추가하였다.

    let g:indentLine_setConceal =0
    let g:indentLine_fileTypeExclude = ['json']



