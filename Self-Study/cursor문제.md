On 02/24/2011 06:06 PM, Gerardo Marset wrote:
> I'm quite new at Vim and I love it already, but this question just won't
> let me sleep.
> Why is it that the cursor doesn't go beyond the last character when in
> command mode?
> I find it kind of wierd, and because of that I have to use either a or A
> to append something to a line (instead of i).
I don't find it weird personally, and I don't think 'a' is any harder to
use than i, and you can also use i and then move cursor one char to the
right, as well.

However, there is a 'virtualedit' option that allows you to move one
char past end of line:

`:set virtualedit=onemore`

see

`:help virtualedit`
