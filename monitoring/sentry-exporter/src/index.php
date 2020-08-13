<?php

global $SENTRY_BASE_URL, $SENTRY_PROJECTS, $SENTRY_TOKEN;
$SENTRY_BASE_URL = "https://sentry_base_url";
$SENTRY_PROJECTS = ["sentry_project"];
$SENTRY_TOKEN = "supply_via_config";

function run() {
  get_env_variables();
 
  $result = "";
  $metrics = array_merge(get_health_metrics(), get_issues_metrics());

  foreach ($metrics as $metric) {
    $result .= $metric.PHP_EOL;
  }
  
  header("Content-Type: text/plain; version=0.0.4");
  echo $result;
}

// let's go
run();
exit;


function get_env_variables() {
  global $SENTRY_BASE_URL, $SENTRY_PROJECTS, $SENTRY_TOKEN;

  if($url = getenv('SENTRY_BASE_URL')) {
    $SENTRY_BASE_URL = $url;
  }

  if($projects = getenv('SENTRY_PROJECTS')) {
    $SENTRY_PROJECTS = explode(',', $projects);
  }

  if($token = getenv('SENTRY_TOKEN')) {
    $SENTRY_TOKEN = $token;
  }
}

function get_health_metrics() {
  global $SENTRY_BASE_URL;

  $metrics = [];
  $health = file_get_contents($SENTRY_BASE_URL."_health/?full");

  if($health){
    $health = json_decode($health);
    $metrics[] = "sentry_warning_status_check ".$health->healthy->WarningStatusCheck;
    $metrics[] = "sentry_celery_app_version_check ".$health->healthy->CeleryAppVersionCheck;
    $metrics[] = "sentry_celery_alive_check ".$health->healthy->CeleryAliveCheck;
    $metrics[] = "sentry_problems_sum ".count($health->problems);
  }

  return $metrics;
}

function get_issues_metrics() {
  global $SENTRY_BASE_URL, $SENTRY_PROJECTS, $SENTRY_TOKEN;

  $metrics = [];

  $opts = array(
    'http' => array(
      'method' => "GET",
      'header' => "Authorization: Bearer ".$SENTRY_TOKEN."\r\n"
    )
  );

  $context = stream_context_create($opts);

  foreach ($SENTRY_PROJECTS as $project) {
    $issues = file_get_contents($SENTRY_BASE_URL."api/0/projects/*PROJECT_NAME*/".$project."/issues/?statsPeriod", false, $context);
    if($issues){
      $issues = json_decode($issues);
      $metrics[] = "sentry_issues_project_".$project."_sum ".count($issues);
    }
  }

  return $metrics;
}