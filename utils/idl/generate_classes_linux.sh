#!/bin/bash


# <path to idlpp> -I <path to idl default> -l <language (e.g java)> <file ".idl">

# Java
/opt/opensplice/V2.2.7/bin/idlpp -I /opt/opensplice/V2.2.7/etc/idl -l java "MSB2ADAPTER.idl"

# CPP
/opt/opensplice/V2.2.7/bin/idlpp -I /opt/opensplice/V2.2.7/etc/idl -l cpp "MSB2ADAPTER.idl"
