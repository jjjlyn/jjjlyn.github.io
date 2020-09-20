[https]://[www.example.com]/[page1] 
(여기서 /page1 은 URI에 해당함)
1. https (protocol)
It can be http, https, ftp, etc.
A protocol is a set of rules that browser use for communication over the network.
'https' is basically a `secure version`

2. www.example.com (domain name)
It is an address of website.

The major difference btw URL and Domain Name
-> URL is a `complete` address
-> Domain Name is `part` of a URL
A domain name with more info is a URL

Domain Name gives a `unique identity` to your website in such a huge web world. No two domain names can be the same. 
When we hit any URL, then that website gets opened with its content. A server serves it. Every computer has an `IP address` which is used for communication over the internet. It is an address as its self explaining 'IP address'. When we hit any URL, then we are actually hitting the IP address of the computer which is responsible for serving the website content(hosting). 
Why does this domain name exist if the IP address is there? You can use IP addresses to get content of the website but you are not be able to remember each website's associated IP address. That's why domain anmes came into the market. 
This huge amount of data is maintained in a database where the domain name with its IP address is stored. A system that stores domain names with its corresponding IP address is known as DNS(Domain Name System). 많은 사람의 휴대폰 번호를 일일이 외우지 못하니, 주소록에 이름을 저장해 놓는 것과 같다.

3. DNS lookup to find IP address
After hitting the URL, the first thing that needs to happen is to resolve IP address associated with the domain name. DNS helps in resolving this. DNS is like a `phone book` and helps us to provide the IP address that is associated with the domain name.
Browser -> youtube.com -> DNS -> 199.223.232.0
But there are `four layers` through which this domain name query goes through.
A. After hitting the URL, the `browser cache` is checked. As browser maintains its DNS records for some amount of time for the websites you have visited earlier. Hence, firstly, DNS query runs here to find the IP address associated with the domain name. (Explore Browser cache)
B. The second place where DNS query runs in `OS cache` followed by router cache. If it is not in the browser cache, the browser will make a `system call`(i.e. gethostname on Windows) to your underlying computer OS to fetch the record since the OS also maintains a cache of DNS records. (Explore OS cache)
C. Checks the `router cache`. If it's not on your computer, the browser will communicate with the router that maintains its own cache of DNS records. (Explore router cache)
D. If in the above steps, a DNS query does not get resolved, then it takes the help of `resolver server`. Resolver server is nothing but your `ISP`(Internet service provider). The query is sent to ISP where DNS query runs in ISP cache. (Explore ISP cache)
E. If in 3rd steps as well, no results found, then request sends to `top or root server of the DNS hierarchy`. If you are searching IP address of the top level domain(.com, .net, .gov, .org), it tells the resolver server to search TLD(Top Level Domain) server. (ISP to explore TLD server) 
We would call the ISP's DNS server a `DNS recursor` whose responsibility is to find the proper IP address of the intended domain name by `asking other DNS servers on the internet` for an answer. Depths 는 보통 3 depth로 이루어져 있음. 쭉 내려가면서 찾는다.
The other DNS servers are called a `name servers` since they perform a DNS search based on the domain architecture of the website domain name.

ISP DNS Server
[Root domain]
"."
[TLD (Top Level Domains)]
edu / org / gov / com / au
[Second-level Domains]
openoffice.org / expedia.gov / microsoft.com / congress.au
[Third-level Domains]
www.expedia.gov / download.microsoft.com / sales.microsoft.org

The DNS recursor will contact the `root name server`. The root name(".") server will redirect it to the `.com` domain name server. .com name server will redirect it to the `google.com` name server. The google.com name server will find the matching IP address for `maps.google.com` in its DNS records and return it to your DNS recursor, which will send it back to your browser. 

These requests are sent using `small data packets` that contain info such as the content of the request and the IP address it is destined for(IP address of the DNS recursor). These packets travel through `multiple networking equipment` btw the client and the server before it reaches the correct DNS server. This equipment use `routing tables` to figure out which way is the fastest possible way for the packet to reach its destination. 
If these packets get lost, you'll get a request failed error. Otherwise, they will reach the correct DNS server, grab the correct IP address, and come back to your browser.

F. Resolver asks TLD server to give IP address of our domain name. TLD stores address information of domain name. It tells the resolver to ask it to Authoritative Name server?? (TLD tells ISP to ask it to ANS) // 이 부분은 잘 모르겠다

G. ANS is responsible for knowing everything about the domain name. Finally, resolver (ISP) gets the IP address and sends it back to the browser. (ANS finds!)

=> The purpose of a DNS query is to search `multiple DNS servers` on the internet until it finds the correct IP address for the website. This type of search is called a `recursive search` since the search will repeatedly continue from a DNS server to a DNS server until it either finds the IP address we need or returns an error response saying it was unable to find it.

After getting an IP address, resolver stores it in its cache so that next time, if the same query comes then it does not have to go to all these steps again. It can now provide IP address from their cache. 

4. `TCP connection` initiates with the server by browser
Once the IP address of the computer(where your website information is there) is found, it initiates conection with the server that matches the IP address to transfer information. To communicate over the network, internet protocol is followed. TCP/IP is the most common protocol used for many types of  HTTP requests. 
To transfer data packets btw your computer(client) and the server, it is important to have a TCP connection established. A connection is built between two using a process called 'TCP 3-way handshake'. This is a three-step process where the client and the server exchange SYN(synchronize) and ACK(acknowledge) messages to establish a connection.

A. A client computer sends a `SYN message` means, whether second computer is open for new connection or not. (Sends a SYN packet asking if it is open for new connections)
B. Then another computer, if open for new connection, it sends `acknowledge message` with `SYN message` as well. (If the server has open port that can accept and initiate new connections, it'll respond with an ACK of the SYN packet using a SYN/ACK packet.)
C. After this, first computer receives its message and acknowledge by sending an `ACK message`. 
=> Then a TCP connection is established for data transmission

5. Communication Starts (Request - Response Process)
The browser sends an HTTP request to the web server. The browser will send a GET request asking for web page. If you're entering credentials or submitting a form, this could be a POST request. This request will also contain additional information such as browser identification(User-Agent header), types of request that it will accept(Accept header), and connection headers asking it to keep the TCP connection alive for additional requests. It will also pass information taken from cookies the browser has in store for this domain.
Responses contain every information that you requested like web page, status-code, cache-control, etc. Now the browser renders the content that has been requested.

6. The server handles the request and sends back a response.
The server contains a `webserver`(i.e. Apache) that receives the request from the browser and passes it to a `request handler` to read and generate a response. The request handler is a program written in ASP, NET, PHP, Ruby, etc that reads the request, its headers, and cookies to check what is being requested, and also update the info on the server if needed. Then it will `assemble a response` in a particular format(`JSON, XML, HTML`).

7. The server sends out an HTTP response.
The server response contains the web page you requested as well as the status code, compression type(Content-Encoding), how to cache the page(Cache-Control), any cookies to set, privacy info, etc.
- 1xx : indicates an informational message only
- 2xx : indicates success of some kind
- 3xx : redirects the client to another URL
- 4xx : indicates an error on the client's part
- 5xx : indicates an error on the server's part

8. The browser displays the HTML content(for HTML responses, which is the most common).
The browser displays the HTML content in phases. First, it will render the bare bone HTML skeleton. Then it will check the HTML `tags` and send out GET requests for additional elements on the web page, such as images, CSS stylesheets, JavaScript files, etc. These `static files` are cached by the browser, so it doesn't have to fetch them again the next time you visit the page. In the end, you'll see the web site appearing on your browser.
