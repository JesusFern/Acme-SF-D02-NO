# README.txt
#
# Copyright (C) 2012-2024 Rafael Corchuelo.
#
# In keeping with the traditional purpose of furthering education and research, it is
# the policy of the copyright owner to permit non-commercial use and redistribution of
# this software. It has been tested carefully, but it is not guaranteed for any particular
# purposes.  The copyright owner does not offer any warranties or representations, nor do
# they accept any liabilities with respect to them.

This is the Acme-Jobs project, which is intended to be a core learning asset for the students
who have enrolled the Design and Testing subject of the Software Engineering curriculum of the 
University of Seville.  This project is intended to illustrate how to develop a simple, but 
non-trivial web information system.

To get this project up and running, please follow the guideline in the theory/lab materials,
taking into account that you must link the appropriate version of the Acme-Framework excluding 
the following resources:

- **/fragments/**
- **/anonymous/user-account/**
- **/authenticated/user-account/**

The previous patterns include the fragments of the master page, as usual, plus the views that 
correspond to an anonymous user creating a new user account, or an authenticated user modifying 
it.  Acme-Jobs extends the default user identity with a phone number and then require to cancel 
the corresponding views in the framework and to provide new ones. 