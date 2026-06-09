import { Goal, Game } from '../types';

const CORS_PROXY = 'https://corsproxy.io/?url=';

function proxyUrl(url: string): string {
  return `${CORS_PROXY}${encodeURIComponent(url)}`;
}

export async function fetchGameGoals(gameId: string): Promise<Goal[]> {
  const rawUrl = `https://api-web.nhle.com/v1/gamecenter/${gameId}/play-by-play`;
  const url = proxyUrl(rawUrl);

  try {
    const response = await fetch(url);
    if (!response.ok) return [];

    const data = await response.json();
    const plays: any[] = data.plays ?? [];
    const roster: any[] = data.rosterSpots ?? [];

    const playerMap = new Map<number, string>();
    const headshotMap = new Map<number, string>();

    for (const spot of roster) {
      const id: number = spot.playerId ?? 0;
      const first: string = spot.firstName?.default ?? '';
      const last: string = spot.lastName?.default ?? '';
      playerMap.set(id, `${first} ${last}`.trim());
      headshotMap.set(id, spot.headshot ?? '');
    }

    const awayId: number = data.awayTeam?.id ?? 0;
    const awayAbbrev: string = data.awayTeam?.abbrev ?? 'AWAY';
    const homeId: number = data.homeTeam?.id ?? 0;
    const homeAbbrev: string = data.homeTeam?.abbrev ?? 'HOME';
    const teamMap = new Map<number, string>([
      [awayId, awayAbbrev],
      [homeId, homeAbbrev],
    ]);

    const goals: Goal[] = [];

    for (const play of plays) {
      if (play.typeDescKey === 'goal') {
        const details = play.details ?? {};
        const scorerId: number = details.scoringPlayerId ?? 0;
        const teamId: number = details.eventOwnerTeamId ?? 0;

        let resolvedPlayer = 'Unknown Player';
        if (playerMap.has(scorerId)) {
          resolvedPlayer = playerMap.get(scorerId)!;
        } else if (details.scoringPlayerName) {
          resolvedPlayer = details.scoringPlayerName;
        } else if (details.scoringPlayerId) {
          resolvedPlayer = `Player #${details.scoringPlayerId}`;
        }

        let resolvedTeam = 'Team';
        if (teamMap.has(teamId)) {
          resolvedTeam = teamMap.get(teamId)!;
        } else {
          resolvedTeam = teamId.toString();
        }

        const resolvedPhoto = headshotMap.get(scorerId) ?? '';

        goals.push({
          eventId: play.eventId ?? 0,
          player: resolvedPlayer,
          playerPhoto: resolvedPhoto,
          team: resolvedTeam,
          period: play.periodDescriptor?.number ?? 1,
          time: play.timeInPeriod ?? '00:00',
          x: (details.xCoord as number) ?? 0,
          y: (details.yCoord as number) ?? 0,
          shotType: details.shotType ?? 'Slap Shot',
        });
      }
    }

    return goals;
  } catch (error) {
    console.error('Error fetching game data:', error);
    return [];
  }
}

export async function fetchTeamGames(
  teamAbbrev: string,
  season: string = '20242025'
): Promise<Game[]> {
  const rawUrl = `https://api-web.nhle.com/v1/club-schedule-season/${teamAbbrev}/${season}`;
  const url = proxyUrl(rawUrl);

  try {
    const response = await fetch(url);
    if (!response.ok) return [];

    const data = await response.json();
    const games: any[] = data.games ?? [];
    const finished: Game[] = [];

    for (const game of games) {
      const state = game.gameState;
      if (state === 'FINAL' || state === 'OFF') {
        finished.push({
          id: game.id ?? 0,
          date: game.gameDate ?? '',
          homeTeam: game.homeTeam?.abbrev ?? '',
          awayTeam: game.awayTeam?.abbrev ?? '',
          homeScore: game.homeTeam?.score ?? 0,
          awayScore: game.awayTeam?.score ?? 0,
          homeLogo: game.homeTeam?.logo ?? '',
          awayLogo: game.awayTeam?.logo ?? '',
          gameState: game.gameState ?? '',
        });
      }
    }

    return finished;
  } catch (error) {
    console.error('Error fetching team games:', error);
    return [];
  }
}
