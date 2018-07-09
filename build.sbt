name := """epolice-backend-mock"""
organization := "ch.glue"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.4"

libraryDependencies += guice
libraryDependencies += "com.auth0" % "java-jwt" % "3.4.0"
