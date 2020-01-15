set rtp+=~/.vim/bundle/Vundle.vim

call vundle#begin()

Plugin 'vimwiki/vimwiki'
Plugin 'VundleVim/Vundle.vim'
Plugin 'plasticboy/vim-markdown'
Plugin 'mhinz/vim-startify'
Plugin 'Yggdroot/indentLine'
Plugin 'sheerun/vim-polyglot'

call vundle#end()

filetype plugin indent on

syntax on
set nocompatible
set background=dark
colorscheme peachpuff 
set history =1000
set number
set smartindent
set autoindent
set shiftwidth=4
set softtabstop=4
set expandtab
set tabstop =4 "Tab을 4칸으로
set cindent " C언어 들여쓰기
set showmatch " 일치하는 괄호 하이라이팅
set smartcase " 대문자가 검색어 문자열에 포함될 때에는 noignorecase
set ignorecase  " 검색시 대소문자 무시
set hlsearch  " 검색시 하이라이트(색상 강조)
set incsearch " 검색 키워드 입력시 한 글자 입력할 때마다 점진 검색
set showbreak=+++\ "윗 줄과 연결된 줄은 '+++"로 시작되어 알아볼 수 있도록 한다.
set linebreak "라인을 끊을 때 단어 단위로 자르기
" set encoding=utf-8
set fileencodings=utf-8,euckr

if has("gui_running")
  if has("gui_gtk2")
    set guifont=Inconsolata\ 16
  elseif has("gui_macvim")
    set guifont=Menlo\ Regular:h20  
  elseif has("gui_win32")
    set guifont=Consolas:h11:cANSI
  endif
endif
set backspace=2
set backspace=indent,eol,start

" 마지막으로 수정된 곳에 커서를 위치함
au BufReadPost *
\ if line("'\"") > 0 && line("'\"") <= line("$") |
\ exe "norm g`\"" |
\ endif

" 명령행 한글 입력 오류 처리
ca ㅈ w
ca ㅑ i
ca ㅂ q
ca ㅈㅂ wq
ca ㅏ k
ca ㅓ j
ca ㅗ h
ca ㅣ l

let g:indentLine_setConceal =0
let g:indentLine_fileTypeExclude = ['json']

let maplocalleader = "\\"

let wiki1 = {}
let wiki1.path = '~/Desktop/daily_study/Self-Study'
let wiki1.path_html = '~/Desktop/daily_study/Self-Study/html/'
let wiki1.ext = '.md'
let wiki1.diary_rel_path = '.'

let wiki2 = {}
let wiki2.path = '~/Desktop/daily_study/To-Do'
let wiki2.path_html = '~/Desktop/daily_study/To-Do/html/'
let wiki2.ext = '.md'
let wiki2.diary_rel_path = '.'

let g:include_set_vimwiki_loaded =1 
let g:vimwiki_list = [wiki1, wiki2]
let g:vimwiki_conceallevel =0

" 자주 사용하는 vimwiki 명령어에 단축키를 취향대로 매핑해둔다
command! WikiIndex :VimwikiIndex
nmap <LocalLeader>ww <Plug>VimwikiIndex
nmap <LocalLeader>wi <Plug>VimwikiDiaryIndex
nmap <LocalLeader>w<LocalLeader>w <Plug>VimwikiMakeDiaryNote

vmap <C-c> "+yi
vmap <C-c> "+yi
vmap <C-c> "+yi
vmap <C-x> "+c
vmap <C-v> c<ESC>"+p
imap <C-v> <ESC>"+pa

" F4 키를 누르면 커서가 놓인 단어를 위키에서 검색한다
nnoremap <F4> :execute "VWS /" . expand("<cword>") . "/" <Bar> :lopen<CR>
