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
set tabstop =4 "Tab�� 4ĭ����
set cindent " C��� �鿩����
set showmatch " ��ġ�ϴ� ��ȣ ���̶�����
set smartcase " �빮�ڰ� �˻��� ���ڿ��� ���Ե� ������ noignorecase
set ignorecase  " �˻��� ��ҹ��� ����
set hlsearch  " �˻��� ���̶���Ʈ(���� ����)
set incsearch " �˻� Ű���� �Է½� �� ���� �Է��� ������ ���� �˻�
set showbreak=+++\ "�� �ٰ� ����� ���� '+++"�� ���۵Ǿ� �˾ƺ� �� �ֵ��� �Ѵ�.
set linebreak "������ ���� �� �ܾ� ������ �ڸ���
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

" ���������� ������ ���� Ŀ���� ��ġ��
au BufReadPost *
\ if line("'\"") > 0 && line("'\"") <= line("$") |
\ exe "norm g`\"" |
\ endif

" ����� �ѱ� �Է� ���� ó��
ca �� w
ca �� i
ca �� q
ca ���� wq
ca �� k
ca �� j
ca �� h
ca �� l

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

" ���� ����ϴ� vimwiki ��ɾ ����Ű�� ������ �����صд�
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

" F4 Ű�� ������ Ŀ���� ���� �ܾ ��Ű���� �˻��Ѵ�
nnoremap <F4> :execute "VWS /" . expand("<cword>") . "/" <Bar> :lopen<CR>
