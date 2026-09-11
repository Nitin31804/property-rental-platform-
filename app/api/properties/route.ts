import { prisma } from "@/lib/prisma";
import { NextResponse } from "next/server";

export const dynamic = "force-dynamic";

export async function GET() {
  try {
    const properties = await prisma.properties.findMany({
      include: { Users: { select: { name: true } } },
      orderBy: { created_at: "desc" }
    });
    return NextResponse.json(properties);
  } catch (error) {
    return NextResponse.json({ error: "Error fetching properties" }, { status: 500 });
  }
}

