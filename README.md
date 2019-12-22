# easy-file-relay
transfer file across multiple nodes using tcp socket

Of course, we can implement file transferring whose source node can not access destination node directly by the means of socket5 or ssh tunnel.
It is not easy to implement and in most cases, we only want to transfer file instead of setting up a vpn.
So we just deploy socket daemon in each host to complete file transferring.
